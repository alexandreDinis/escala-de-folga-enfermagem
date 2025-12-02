package com.oroboros.EscalaDeFolga.domain.service.alerta;

import com.oroboros.EscalaDeFolga.domain.model.alerta.Alerta;
import com.oroboros.EscalaDeFolga.domain.model.alerta.TipoAlertaEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gera alertas quando colaborador não tem domingo no mês
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlertaDomingoFaltando implements IAlertaGenerator {

    private final FolgaRepository folgaRepository;

    @Override
    public List<Alerta> gerarAlertas(Folga folga) {
        List<Alerta> alertas = new ArrayList<>();

        var escala = folga.getEscala();
        var colaborador = folga.getColaborador();

        // Conta folgas do colaborador no mês
        long totalFolgas = folgaRepository.countByColaboradorAndEscalaAndStatusIn(
                colaborador,
                escala,
                List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA)
        );

        // Verifica se tem domingo
        boolean temDomingo = folgaRepository.existsFolgaDomingoNoMes(
                colaborador,
                escala.getMes(),
                escala.getAno()
        );

        // Se não tem domingo E ainda faltam folgas, gera alerta
        if (!temDomingo && totalFolgas < escala.getFolgasPermitidas()) {
            List<LocalDate> domingosMes = obterDomingosMes(escala.getAno(), escala.getMes());

            String domingosFormatados = domingosMes.stream()
                    .map(d -> d.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM")))
                    .collect(Collectors.joining(", "));

            String mensagem = String.format(
                    "%s ainda não possui folga em domingo em %02d/%d. " +
                            "Folgas restantes: %d. Domingos disponíveis: %s",
                    colaborador.getNome(),
                    escala.getMes(),
                    escala.getAno(),
                    escala.getFolgasPermitidas() - totalFolgas,
                    domingosFormatados
            );

            String recomendacao = String.format(
                    "Agende uma das próximas folgas para um domingo: %s",
                    domingosFormatados
            );

            Alerta alerta = Alerta.criar(
                    escala,
                    colaborador,
                    folga,
                    TipoAlertaEnum.AVISO_DOMINGO_FALTANDO,
                    mensagem,
                    recomendacao
            );

            alertas.add(alerta);
            log.info("✅ Alerta de domingo faltando gerado para {}", colaborador.getNome());
        }

        return alertas;
    }

    /**
     * Obtém todos os domingos do mês
     */
    private List<LocalDate> obterDomingosMes(int ano, int mes) {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        List<LocalDate> domingos = new ArrayList<>();

        LocalDate data = yearMonth.atDay(1);
        LocalDate fim = yearMonth.atEndOfMonth();

        while (!data.isAfter(fim)) {
            if (data.getDayOfWeek() == DayOfWeek.SUNDAY) {
                domingos.add(data);
            }
            data = data.plusDays(1);
        }

        return domingos;
    }
}
