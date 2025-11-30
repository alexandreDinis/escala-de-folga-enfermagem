package com.oroboros.EscalaDeFolga.domain.service;

import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.exception.BusinessException;
import com.oroboros.EscalaDeFolga.infrastructure.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service de Calendário - trabalha apenas com entidades e classes de domínio
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarioService {

    private final EscalaRepository escalaRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final FolgaRepository folgaRepository;
    private final EscalaRegrasService regrasService;

    /**
     * Gera dados do calendário do mês (retorna domínio, não DTO)
     */
    public CalendarioDomain gerarCalendario(Long escalaId) {
        log.info("📅 Gerando calendário para escala {}", escalaId);

        Escala escala = escalaRepository.findById(escalaId)
                .orElseThrow(() -> new BusinessException("Escala", escalaId));

        List<Colaborador> colaboradores = colaboradorRepository
                .findBySetorAndTurno(escala.getSetor(), escala.getTurno());

        if (colaboradores.isEmpty()) {
            throw new BusinessException(
                    "Não há colaboradores cadastrados para este turno/setor"
            );
        }

        // Gera dias do mês
        List<DiaDomain> dias = gerarDiasMes(escala, colaboradores);

        // Gera colaboradores com histórico
        List<ColaboradorHistoricoDomain> colaboradoresHistorico =
                gerarColaboradoresComHistorico(escala, colaboradores);

        // Calcula resumo
        ResumoCalendarioDomain resumo = calcularResumo(escala, dias, colaboradores);

        // Configuração
        ConfiguracaoCalendarioDomain configuracao = obterConfiguracao(escala);

        return new CalendarioDomain(
                escala,
                dias,
                colaboradoresHistorico,
                resumo,
                configuracao
        );
    }

    /**
     * Verifica se há colaboradores sem última folga registrada
     */
    public AvisoHistoricoDomain verificarHistoricoColaboradores(Long escalaId) {
        Escala escala = escalaRepository.findById(escalaId)
                .orElseThrow(() -> new BusinessException("Escala", escalaId));

        // Busca todos os colaboradores do setor/turno
        List<Colaborador> colaboradores = colaboradorRepository
                .findBySetorAndTurno(escala.getSetor(), escala.getTurno());

        // Filtra colaboradores sem última folga ou com folga antiga (>6 dias)
        LocalDate dataLimite = LocalDate.now().minusDays(6);

        List<Colaborador> colaboradoresSemHistorico = colaboradores.stream()
                .filter(c -> c.getUltimaFolga() == null || c.getUltimaFolga().isBefore(dataLimite))
                .toList();

        boolean temAvisos = !colaboradoresSemHistorico.isEmpty();

        String mensagem = temAvisos
                ? String.format("Há %d colaborador(es) sem referência de última folga nos últimos 6 dias. " +
                        "Cadastre a última folga para garantir distribuição correta.",
                colaboradoresSemHistorico.size())
                : "Todos os colaboradores possuem histórico de folgas atualizado.";

        return new AvisoHistoricoDomain(temAvisos, mensagem, colaboradoresSemHistorico);
    }

    /**
     * Domain para avisos de histórico
     */
    public record AvisoHistoricoDomain(
            boolean temAvisos,
            String mensagem,
            List<Colaborador> colaboradores
    ) {}

    /**
     * Gera lista de dias do mês
     */
    private List<DiaDomain> gerarDiasMes(Escala escala, List<Colaborador> colaboradores) {
        YearMonth mes = YearMonth.of(escala.getAno(), escala.getMes());
        LocalDate primeiroDia = mes.atDay(1);
        LocalDate ultimoDia = mes.atEndOfMonth();

        List<DiaDomain> dias = new ArrayList<>();

        for (LocalDate data = primeiroDia; !data.isAfter(ultimoDia); data = data.plusDays(1)) {
            dias.add(gerarDia(data, escala, colaboradores));
        }

        return dias;
    }

    /**
     * Gera informações de um dia
     */
    private DiaDomain gerarDia(LocalDate data, Escala escala, List<Colaborador> colaboradores) {
        List<Folga> folgasNoDia = folgaRepository.findByDataSolicitadaAndEscala(data, escala);

        List<Folga> folgasAtivas = folgasNoDia.stream()
                .filter(f -> f.getStatus() == StatusFolgaEnum.APROVADA ||
                        f.getStatus() == StatusFolgaEnum.PENDENTE)
                .collect(Collectors.toList());

        int totalFolgasNoDia = folgasAtivas.size();
        int colaboradoresDisponiveis = colaboradores.size() - totalFolgasNoDia;
        int limiteMaximo = (int) Math.floor(colaboradores.size() * 0.5);

        StatusDiaDomain status = determinarStatusDia(data, totalFolgasNoDia, limiteMaximo);

        return new DiaDomain(
                data,
                status,
                totalFolgasNoDia,
                colaboradoresDisponiveis,
                limiteMaximo,
                folgasAtivas
        );
    }

    /**
     * Determina status do dia
     */
    private StatusDiaDomain determinarStatusDia(LocalDate data, int totalFolgas, int limiteMaximo) {
        if (data.isBefore(LocalDate.now())) {
            return new StatusDiaDomain("PASSADO", "#BDBDBD", false, "Data passada");
        }

        if (totalFolgas >= limiteMaximo) {
            return new StatusDiaDomain("OCUPADO", "#F44336", false, "Limite atingido");
        }

        if (data.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return new StatusDiaDomain("DOMINGO", "#2196F3", true, "");
        }

        if (totalFolgas >= limiteMaximo * 0.7) {
            return new StatusDiaDomain("ALERTA", "#FF9800", true, "Próximo ao limite");
        }

        return new StatusDiaDomain("ABERTO", "#4CAF50", true, "");
    }

    /**
     * Gera colaboradores com histórico
     */
    private List<ColaboradorHistoricoDomain> gerarColaboradoresComHistorico(
            Escala escala,
            List<Colaborador> colaboradores
    ) {
        return colaboradores.stream()
                .map(c -> gerarColaboradorHistorico(c, escala))
                .sorted(Comparator.comparing(ColaboradorHistoricoDomain::nome))
                .collect(Collectors.toList());
    }

    /**
     * Gera histórico de um colaborador
     */
    private ColaboradorHistoricoDomain gerarColaboradorHistorico(
            Colaborador colaborador,
            Escala escala
    ) {
        List<Folga> folgas = folgaRepository.findByColaboradorAndEscalaAndStatusIn(
                colaborador,
                escala,
                List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA)
        );

        LocalDate ultimaFolga = colaborador.getUltimaFolga();
        int diasDesdeUltima = ultimaFolga != null ?
                (int) ChronoUnit.DAYS.between(ultimaFolga, LocalDate.now()) : 999;

        LocalDate proximaData = calcularProximaDataDisponivel(colaborador);

        boolean temDomingo = folgas.stream()
                .anyMatch(f -> f.getDataSolicitada().getDayOfWeek() == DayOfWeek.SUNDAY);

        List<String> alertas = new ArrayList<>();
        if (!temDomingo) alertas.add("Sem domingo neste mês");
        if (folgas.size() < escala.getFolgasPermitidas() / 2) {
            alertas.add("Poucas folgas alocadas");
        }

        String cor = determinarCorColaborador(diasDesdeUltima, folgas.size(), escala.getFolgasPermitidas());

        return new ColaboradorHistoricoDomain(
                colaborador,
                ultimaFolga,
                diasDesdeUltima,
                proximaData,
                folgas.size(),
                escala.getFolgasPermitidas() - folgas.size(),
                folgas.stream().map(Folga::getDataSolicitada).collect(Collectors.toList()),
                temDomingo,
                folgas.size() < escala.getFolgasPermitidas() / 2,
                alertas,
                diasDesdeUltima >= regrasService.getDiasTrabalhoPermitidos(),
                cor
        );
    }

    private LocalDate calcularProximaDataDisponivel(Colaborador colaborador) {
        LocalDate ultimaFolga = colaborador.getUltimaFolga();
        if (ultimaFolga == null) {
            return LocalDate.now();
        }
        return ultimaFolga.plusDays(regrasService.getDiasTrabalhoPermitidos() + 1);
    }

    private String determinarCorColaborador(int diasDesdeUltima, int totalFolgas, int folgasPermitidas) {
        if (diasDesdeUltima >= 6) return "#4CAF50";
        if (totalFolgas < folgasPermitidas / 2) return "#FF9800";
        return "#2196F3";
    }

    private ResumoCalendarioDomain calcularResumo(
            Escala escala,
            List<DiaDomain> dias,
            List<Colaborador> colaboradores
    ) {
        int totalFolgas = dias.stream()
                .mapToInt(DiaDomain::totalFolgasNoDia)
                .sum();

        int totalDisponiveis = colaboradores.size() * escala.getFolgasPermitidas();
        int percentual = totalDisponiveis > 0 ?
                (totalFolgas * 100) / totalDisponiveis : 0;

        long diasAbertos = dias.stream()
                .filter(d -> "ABERTO".equals(d.status().nome()) || "DOMINGO".equals(d.status().nome()))
                .count();

        long diasOcupados = dias.stream()
                .filter(d -> "OCUPADO".equals(d.status().nome()))
                .count();

        long diasComAlerta = dias.stream()
                .filter(d -> "ALERTA".equals(d.status().nome()))
                .count();

        return new ResumoCalendarioDomain(
                colaboradores.size(),
                totalFolgas,
                totalDisponiveis - totalFolgas,
                percentual,
                (int) diasAbertos,
                (int) diasOcupados,
                (int) diasComAlerta
        );
    }

    private ConfiguracaoCalendarioDomain obterConfiguracao(Escala escala) {
        return new ConfiguracaoCalendarioDomain(
                escala.getFolgasPermitidas(),
                regrasService.getDiasTrabalhoPermitidos(),
                5,
                true,
                5
        );
    }

    /**
     * ========================================
     * CLASSES DE DOMÍNIO (não são DTOs!)
     * ========================================
     */

    public record CalendarioDomain(
            Escala escala,
            List<DiaDomain> dias,
            List<ColaboradorHistoricoDomain> colaboradores,
            ResumoCalendarioDomain resumo,
            ConfiguracaoCalendarioDomain configuracao
    ) {}

    public record DiaDomain(
            LocalDate data,
            StatusDiaDomain status,
            int totalFolgasNoDia,
            int colaboradoresDisponiveis,
            int limiteMaximoFolgas,
            List<Folga> folgasNoDia
    ) {}

    public record StatusDiaDomain(
            String nome,
            String cor,
            boolean clicavel,
            String motivo
    ) {}

    public record ColaboradorHistoricoDomain(
            Colaborador colaborador,
            LocalDate ultimaFolga,
            int diasDesdeUltimaFolga,
            LocalDate proximaDataDisponivel,
            int totalFolgasNoMes,
            int folgasRestantes,
            List<LocalDate> datasComFolga,
            boolean temDomingo,
            boolean emRisco,
            List<String> alertas,
            boolean podeFolgarHoje,
            String corStatus
    ) {
        public Long id() { return colaborador.getId(); }
        public String nome() { return colaborador.getNome(); }
        public String turno() { return colaborador.getTurno().name(); }
    }

    public record ResumoCalendarioDomain(
            int totalColaboradores,
            int totalFolgasAlocadas,
            int totalFolgasDisponiveis,
            int percentualOcupacao,
            int diasAbertos,
            int diasOcupados,
            int diasComAlerta
    ) {}

    public record ConfiguracaoCalendarioDomain(
            int folgasPermitidasPorColaborador,
            int diasTrabalhoMaximo,
            int intervaloMinimoRecomendado,
            boolean domingoObrigatorio,
            int limiteFolgasSimultaneas
    ) {}
}
