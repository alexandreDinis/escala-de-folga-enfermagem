package com.oroboros.EscalaDeFolga.domain.validation;

import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import com.oroboros.EscalaDeFolga.domain.service.EscalaRegrasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Validador responsável por garantir que o colaborador não fique nenhuma semana
 * sem folga durante o mês, conforme exigido pela legislação trabalhista.
 *
 * <p>A legislação brasileira (CLT) determina que o trabalhador deve ter pelo menos
 * uma folga semanal, preferencialmente aos domingos. Este validador verifica se,
 * ao adicionar a nova {@link Folga} solicitada, o colaborador terá pelo menos
 * uma folga em cada semana do mês.</p>
 *
 * <h3>Critérios de validação:</h3>
 * <ul>
 *   <li>Divide o mês da escala em semanas (de segunda a domingo).</li>
 *   <li>Verifica se o colaborador possui ou terá pelo menos 1 folga em cada semana.</li>
 *   <li>Considera folgas com status {@link StatusFolgaEnum#PENDENTE} e {@link StatusFolgaEnum#APROVADA}.</li>
 *   <li>Valida se a distribuição das folgas (incluindo a nova) deixará alguma semana descoberta.</li>
 *   <li>Se houver semana(s) sem folga após a solicitação, bloqueia a criação.</li>
 * </ul>
 *
 * <h3>Exemplo de cenário bloqueado:</h3>
 * <pre>
 * - Escala: Maio/2025 (4 folgas permitidas)
 * - Folgas já aprovadas: 05/05 (semana 1), 06/05 (semana 1), 12/05 (semana 2)
 * - Nova solicitação: 13/05 (semana 2)
 * - Resultado: Total de 4 folgas, mas semanas 3, 4 e 5 ficariam sem folga
 * - Status: BLOQUEADO
 * </pre>
 *
 * <h3>Exemplo de cenário aprovado:</h3>
 * <pre>
 * - Escala: Maio/2025 (4 folgas permitidas)
 * - Folgas já aprovadas: 05/05 (semana 1), 12/05 (semana 2), 19/05 (semana 3)
 * - Nova solicitação: 26/05 (semana 4)
 * - Resultado: Todas as 4 semanas completas do mês terão pelo menos 1 folga
 * - Status: APROVADO
 * </pre>
 *
 * @author Alexandre Dinis
 * @see IFolgaValidator
 * @see FolgaRepository
 * @see EscalaRegrasService
 * @see Escala
 */
@Component
@RequiredArgsConstructor
public class ValidaDistribuicaoSemanalDeFolgas implements IFolgaValidator {

    private final FolgaRepository folgaRepository;
    private final EscalaRegrasService escalaRegrasService;

    @Override
    public ResultadoValidacao validar(Folga folga) {
        Escala escala = folga.getEscala();
        Colaborador colaborador = folga.getColaborador();
        LocalDate dataSolicitada = folga.getDataSolicitada();

        // Busca todas as folgas já solicitadas pelo colaborador nesta escala
        List<Folga> folgasExistentes = folgaRepository.findByColaboradorAndEscalaAndStatusIn(
                colaborador,
                escala,
                List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA)
        );

        // Adiciona a folga que está sendo solicitada à lista para simular o cenário
        List<LocalDate> todasAsDatasComFolga = folgasExistentes.stream()
                .map(Folga::getDataSolicitada)
                .collect(Collectors.toCollection(ArrayList::new));
        todasAsDatasComFolga.add(dataSolicitada);

        // Calcula quantas folgas ainda poderão ser solicitadas
        long folgasRestantes = escala.getFolgasPermitidas() - todasAsDatasComFolga.size();

        // Identifica as semanas do mês e verifica se há folgas em cada uma
        YearMonth mesEscala = YearMonth.of(escala.getAno(), escala.getMes());
        Map<Integer, List<LocalDate>> folgasPorSemana = agruparFolgasPorSemana(
                todasAsDatasComFolga,
                mesEscala
        );

        Set<Integer> semanasDoMes = obterSemanasDoMes(mesEscala);
        Set<Integer> semanasComFolga = folgasPorSemana.keySet();

        // Identifica semanas sem folga
        Set<Integer> semanasSemFolga = new HashSet<>(semanasDoMes);
        semanasSemFolga.removeAll(semanasComFolga);

        // Se não há mais folgas restantes e ainda existem semanas sem folga, bloqueia
        if (folgasRestantes == 0 && !semanasSemFolga.isEmpty()) {
            return ResultadoValidacao.erro(String.format(
                    "Não é possível solicitar folga em %s. " +
                            "O colaborador %s atingirá o limite de %d folga(s) permitida(s), " +
                            "mas ficará sem folga na(s) semana(s): %s do mês %02d/%d. " +
                            "A legislação trabalhista exige pelo menos %d folga por semana. " +
                            "Redistribua as folgas para garantir cobertura em todas as semanas.",
                    dataSolicitada.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    colaborador.getNome(),
                    escala.getFolgasPermitidas(),
                    formatarSemanas(semanasSemFolga, mesEscala),
                    escala.getMes(),
                    escala.getAno(),
                    EscalaRegrasService.MIN_FOLGAS_POR_SEMANA
            ));
        }

        // Verifica se há concentração excessiva de folgas em uma mesma semana
        for (Map.Entry<Integer, List<LocalDate>> entry : folgasPorSemana.entrySet()) {
            int semana = entry.getKey();
            int folgasNaSemana = entry.getValue().size();

            // Se uma semana tem mais de 2 folgas e ainda há semanas sem folga, alerta
            if (folgasNaSemana > 2 && !semanasSemFolga.isEmpty()) {
                return ResultadoValidacao.erro(String.format(
                        "Não é possível solicitar folga em %s. " +
                                "A semana %d já possui %d folga(s) (%s), " +
                                "enquanto a(s) semana(s) %s ainda não possui(em) nenhuma. " +
                                "Redistribua as folgas para garantir pelo menos 1 folga por semana.",
                        dataSolicitada.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        semana,
                        folgasNaSemana,
                        formatarDatas(entry.getValue()),
                        formatarSemanas(semanasSemFolga, mesEscala)
                ));
            }
        }

        return ResultadoValidacao.ok();
    }

    /**
     * Agrupa as datas de folga por número da semana do mês
     */
    private Map<Integer, List<LocalDate>> agruparFolgasPorSemana(
            List<LocalDate> datas,
            YearMonth mes) {

        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);

        return datas.stream()
                .filter(data -> YearMonth.from(data).equals(mes))
                .collect(Collectors.groupingBy(
                        data -> obterNumeroSemanaNomes(data, mes),
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

    /**
     * Obtém todas as semanas que existem no mês
     * (considera apenas semanas que têm pelo menos um dia útil no mês)
     */
    private Set<Integer> obterSemanasDoMes(YearMonth mes) {
        Set<Integer> semanas = new TreeSet<>();
        LocalDate primeiroDia = mes.atDay(1);
        LocalDate ultimoDia = mes.atEndOfMonth();

        for (LocalDate data = primeiroDia; !data.isAfter(ultimoDia); data = data.plusDays(1)) {
            semanas.add(obterNumeroSemanaNomes(data, mes));
        }

        return semanas;
    }

    /**
     * Calcula o número da semana dentro do mês (1-5)
     */
    private int obterNumeroSemanaNomes(LocalDate data, YearMonth mes) {
        LocalDate primeiroDia = mes.atDay(1);
        LocalDate inicioSemana = data.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        if (inicioSemana.isBefore(primeiroDia)) {
            inicioSemana = primeiroDia;
        }

        int diasDesdeInicio = (int) java.time.temporal.ChronoUnit.DAYS.between(primeiroDia, inicioSemana);
        return (diasDesdeInicio / 7) + 1;
    }

    /**
     * Formata as semanas para exibição na mensagem
     */
    private String formatarSemanas(Set<Integer> semanas, YearMonth mes) {
        return semanas.stream()
                .map(sem -> {
                    LocalDate inicioSemana = obterInicioSemana(sem, mes);
                    LocalDate fimSemana = obterFimSemana(sem, mes);
                    return String.format("%d (%s a %s)",
                            sem,
                            inicioSemana.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM")),
                            fimSemana.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM"))
                    );
                })
                .collect(Collectors.joining(", "));
    }

    /**
     * Formata lista de datas
     */
    private String formatarDatas(List<LocalDate> datas) {
        return datas.stream()
                .map(d -> d.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM")))
                .collect(Collectors.joining(", "));
    }

    /**
     * Obtém a data de início de uma semana específica do mês
     */
    private LocalDate obterInicioSemana(int numeroSemana, YearMonth mes) {
        LocalDate primeiroDia = mes.atDay(1);
        LocalDate inicioDaSemana = primeiroDia.plusWeeks(numeroSemana - 1)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        if (inicioDaSemana.isBefore(primeiroDia)) {
            return primeiroDia;
        }
        return inicioDaSemana;
    }

    /**
     * Obtém a data de fim de uma semana específica do mês
     */
    private LocalDate obterFimSemana(int numeroSemana, YearMonth mes) {
        LocalDate ultimoDia = mes.atEndOfMonth();
        LocalDate fimDaSemana = obterInicioSemana(numeroSemana, mes)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        if (fimDaSemana.isAfter(ultimoDia)) {
            return ultimoDia;
        }
        return fimDaSemana;
    }
}
