package com.oroboros.EscalaDeFolga.domain.service;

import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.domain.model.alerta.Alerta;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import com.oroboros.EscalaDeFolga.domain.validation.folga.FolgaValidatorComposite;
import com.oroboros.EscalaDeFolga.domain.exception.BusinessException;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import com.oroboros.EscalaDeFolga.domain.service.alerta.AlertaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service de Folga - trabalha apenas com entidades
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FolgaService {

    private final FolgaRepository folgaRepository;
    private final FolgaValidatorComposite validadores;
    private final AlertaService alertaService;
    private final EscalaRegrasService regrasService;

    /**
     * Cria folga (recebe e retorna ENTIDADE)
     */
    @Transactional
    public Folga criarFolga(Folga folga) {
        log.info("📝 Criando folga: colaborador={}, data={}",
                folga.getColaborador().getId(),
                folga.getDataSolicitada()
        );

        // Define status inicial
        folga.setStatus(StatusFolgaEnum.PENDENTE);

        // ========================================
        // ✅ FASE 1: VALIDAÇÕES (BLOQUEIAM)
        // ========================================
        ResultadoValidacao validacao = validadores.validar(folga);
        if (!validacao.isValido()) {
            log.warn("❌ Folga rejeitada: {}", validacao.getMensagem());
            throw new BusinessException(validacao.getMensagem());
        }

        // ========================================
        // ✅ FASE 2: PERSISTIR FOLGA
        // ========================================
        Folga criada = folgaRepository.save(folga);
        log.info("✅ Folga criada: ID={}", criada.getId());

        // Atualiza última folga do colaborador
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

        // Revalida após alteração
        ResultadoValidacao validacao = validadores.validar(folga);
        if (!validacao.isValido()) {
            throw new BusinessException(validacao.getMensagem());
        }

        return folgaRepository.save(folga);
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
