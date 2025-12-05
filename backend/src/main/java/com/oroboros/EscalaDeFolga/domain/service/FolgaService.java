package com.oroboros.EscalaDeFolga.domain.service;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.domain.model.alerta.Alerta;
import com.oroboros.EscalaDeFolga.app.dto.folga.ValidacaoUltimaFolgaResponseDTO;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import com.oroboros.EscalaDeFolga.domain.validation.folga.FolgaValidatorComposite;
import com.oroboros.EscalaDeFolga.domain.exception.BusinessException;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import com.oroboros.EscalaDeFolga.domain.service.alerta.AlertaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service de Folga - trabalha apenas com entidades
 */
@Slf4j
@Service
public class FolgaService {

    private final FolgaRepository folgaRepository;
    private final FolgaValidatorComposite validadores;
    private final AlertaService alertaService;
    private final EscalaRegrasService regrasService;
    private final ColaboradorService colaboradorService;
    private final EscalaService escalaService;

    // ✅ Construtor com @Lazy para quebrar ciclo
    public FolgaService(
            FolgaRepository folgaRepository,
            FolgaValidatorComposite validadores,
            AlertaService alertaService,
            EscalaRegrasService regrasService,
            ColaboradorService colaboradorService,
            @Lazy EscalaService escalaService
    ) {
        this.folgaRepository = folgaRepository;
        this.validadores = validadores;
        this.alertaService = alertaService;
        this.regrasService = regrasService;
        this.colaboradorService = colaboradorService;
        this.escalaService = escalaService;
    }

    /**
     * Cria folga (recebe e retorna ENTIDADE)
     */
    @Transactional
    public Folga criarFolga(Folga folga) {
        log.info("📝 Criando folga: colaborador={}, data={}",
                folga.getColaborador().getId(),
                folga.getDataSolicitada()
        );

        folga.setStatus(StatusFolgaEnum.PENDENTE);

        ResultadoValidacao validacao = validadores.validar(folga);
        if (!validacao.isValido()) {
            log.warn("❌ Folga rejeitada: {}", validacao.getMensagem());
            throw new BusinessException(validacao.getMensagem());
        }

        Folga criada = folgaRepository.save(folga);
        log.info("✅ Folga criada: ID={}", criada.getId());

        criada.getColaborador().setUltimaFolga(criada.getDataSolicitada());

        return criada;
    }

    /**
     * Gera alertas para uma folga (retorna lista de entidades)
     */
    @Transactional
    public List<Alerta> gerarAlertas(Folga folga) {
        log.info("🔔 Gerando alertas para folga ID={}", folga.getId());
        return alertaService.gerarEPersistirAlertas(folga);
    }

    /**
     * Calcula próximas datas disponíveis (retorna domínio)
     */
    public ProximasFolgasDomain calcularProximasDatas(Folga folga) {
        LocalDate proximaData = folga.getDataSolicitada()
                .plusDays(regrasService.getDiasTrabalhoPermitidos() + 1);

        List<LocalDate> proximasCinco = new java.util.ArrayList<>();
        LocalDate data = proximaData;

        for (int i = 0; i < 5 && proximasCinco.size() < 5; i++) {
            proximasCinco.add(data);
            data = data.plusDays(1);
        }

        int diasAteProxima = (int) java.time.temporal.ChronoUnit.DAYS.between(
                LocalDate.now(),
                proximaData
        );

        return new ProximasFolgasDomain(
                proximaData,
                proximasCinco,
                Math.max(0, diasAteProxima)
        );
    }

    /**
     * Busca folga por ID (retorna entidade)
     */
    public Folga buscarPorId(Long id) {
        return folgaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Folga", id));
    }

    /**
     * Deleta folga pendente
     */
    @Transactional
    public void deletar(Long id) {
        Folga folga = buscarPorId(id);

        if (folga.getStatus() != StatusFolgaEnum.PENDENTE) {
            throw new BusinessException("Apenas folgas pendentes podem ser deletadas");
        }

        folgaRepository.delete(folga);
        log.info("✅ Folga {} deletada", id);
    }

    /**
     * Atualiza folga pendente
     */
    @Transactional
    public Folga atualizar(Long id, LocalDate novaData, String novaJustificativa) {
        Folga folga = buscarPorId(id);

        if (folga.getStatus() != StatusFolgaEnum.PENDENTE) {
            throw new BusinessException("Apenas folgas pendentes podem ser atualizadas");
        }

        if (novaData != null) {
            folga.setDataSolicitada(novaData);
        }

        if (novaJustificativa != null) {
            folga.setJustificativa(novaJustificativa);
        }

        ResultadoValidacao validacao = validadores.validar(folga);
        if (!validacao.isValido()) {
            throw new BusinessException(validacao.getMensagem());
        }

        return folgaRepository.save(folga);
    }

    /**
     * Cria registro de folga histórica (sem validações de regras de negócio)
     * Usado apenas para cadastrar histórico de folgas antigas
     */
    @Transactional
    public void criarFolgaHistorica(Colaborador colaborador, LocalDate dataFolga) {
        Folga folgaHistorica = new Folga();
        folgaHistorica.setColaborador(colaborador);
        folgaHistorica.setDataSolicitada(dataFolga);
        folgaHistorica.setStatus(StatusFolgaEnum.APROVADA);
        folgaHistorica.setJustificativa("Histórico cadastrado manualmente");
        folgaHistorica.setEscala(null);

        folgaRepository.save(folgaHistorica);
    }

    /**
     * Cadastra histórico de última folga para um colaborador
     * ✅ Agora valida a data contra a escala (se fornecida)
     */
    public void cadastrarHistorico(Long colaboradorId, LocalDate dataUltimaFolga, Long escalaId) {
        log.info("📝 Cadastrando histórico: colaborador={}, data={}, escala={}",
                colaboradorId, dataUltimaFolga, escalaId);

        // ✅ Validação 1: Se escalaId foi fornecido, validar contra a escala
        if (escalaId != null) {
            log.info("🔍 Validando contra escala: {}", escalaId);

            Escala escala = escalaService.buscarPorId(escalaId);
            int diasTrabalhoMaximo = regrasService.getDiasTrabalhoPermitidos();
            LocalDate primeiroDiaEscala = LocalDate.of(escala.getAno(), escala.getMes(), 1);
            LocalDate dataMinimPermitida = primeiroDiaEscala.minusDays(diasTrabalhoMaximo + 1);

            log.info("🔍 Validando: data={}, dataMinima={}", dataUltimaFolga, dataMinimPermitida);

            if (dataUltimaFolga.isBefore(dataMinimPermitida)) {
                String mensagem = String.format(
                        "Data da última folga não pode ser anterior a %s. " +
                                "Colaborador precisa de %d dias de trabalho antes de %s.",
                        dataMinimPermitida.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        diasTrabalhoMaximo,
                        primeiroDiaEscala.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
                log.warn("❌ Data inválida: {}", mensagem);
                throw new BusinessException(mensagem);
            }
        } else {
            log.info("⚠️ Sem escalaId, apenas registrando histórico");
        }

        // ✅ Se passou nas validações, atualiza
        log.info("✅ Data válida, atualizando colaborador");
        colaboradorService.atualizarUltimaFolga(colaboradorId, dataUltimaFolga);
    }

    /**
     * Verifica se o colaborador tem histórico válido para a escala
     */
    public boolean temHistoricoValido(Long colaboradorId, Escala escala) {
        Colaborador colaborador = colaboradorService.buscarPorId(colaboradorId);

        if (colaborador.getUltimaFolga() == null) {
            log.warn("⚠️ Colaborador {} SEM última folga", colaboradorId);
            return false;
        }

        int diasTrabalhoMaximo = regrasService.getDiasTrabalhoPermitidos();
        LocalDate primeiroDiaEscala = LocalDate.of(escala.getAno(), escala.getMes(), 1);
        LocalDate dataMinimPermitida = primeiroDiaEscala.minusDays(diasTrabalhoMaximo + 1);
        LocalDate ultimaFolga = colaborador.getUltimaFolga();

        boolean valido = ultimaFolga.isAfter(dataMinimPermitida) || ultimaFolga.isEqual(dataMinimPermitida);

        log.info("📅 Verificando histórico: colaborador={}, ultimaFolga={}, dataMinima={}, valido={}",
                colaboradorId, ultimaFolga, dataMinimPermitida, valido);

        return valido;
    }

    /**
     * Valida se a data da última folga é permitida para a escala
     */
    public ValidacaoUltimaFolgaResponseDTO validarDataUltimaFolga(
            Long escalaId,
            LocalDate dataSolicitada
    ) {
        Escala escala = escalaService.buscarPorId(escalaId);

        int diasTrabalhoMaximo = regrasService.getDiasTrabalhoPermitidos();
        LocalDate primeiroDiaEscala = LocalDate.of(escala.getAno(), escala.getMes(), 1);
        LocalDate dataMinimPermitida = primeiroDiaEscala.minusDays(diasTrabalhoMaximo + 1);

        log.info("🔍 [DEBUG VALIDAÇÃO]");
        log.info("   Escala: {}/{}", escala.getMes(), escala.getAno());
        log.info("   Primeiro dia: {}", primeiroDiaEscala);
        log.info("   Dias trabalho máximo: {}", diasTrabalhoMaximo);
        log.info("   Data mínima permitida: {}", dataMinimPermitida);
        log.info("   Data solicitada: {}", dataSolicitada);
        log.info("   É antes? {}", dataSolicitada.isBefore(dataMinimPermitida));
        log.info("   É igual? {}", dataSolicitada.isEqual(dataMinimPermitida));
        log.info("   É depois? {}", dataSolicitada.isAfter(dataMinimPermitida));

        // ✅ CRÍTICO: Usar isBefore (não isBeforeOrEqual)
        if (dataSolicitada.isBefore(dataMinimPermitida)) {
            String mensagem = String.format(
                    "Data da última folga não pode ser anterior a %s. " +
                            "Colaborador precisa de %d dias de trabalho antes de %s.",
                    dataMinimPermitida.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    diasTrabalhoMaximo,
                    primeiroDiaEscala.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );

            log.warn("❌ Data inválida: {}", mensagem);

            return new ValidacaoUltimaFolgaResponseDTO(
                    false,
                    mensagem,
                    dataMinimPermitida,
                    diasTrabalhoMaximo
            );
        }

        log.info("✅ Data válida");

        return new ValidacaoUltimaFolgaResponseDTO(
                true,
                "Data válida",
                dataMinimPermitida,
                diasTrabalhoMaximo
        );
    }

    /**
     * Classe de domínio para próximas folgas (não é DTO!)
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class ProximasFolgasDomain {
        private LocalDate proximaDataDisponivel;
        private List<LocalDate> proximasCincoDatas;
        private int diasAteProximaFolga;
    }
}
