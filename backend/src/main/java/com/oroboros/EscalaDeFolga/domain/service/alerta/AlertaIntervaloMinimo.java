package com.oroboros.EscalaDeFolga.domain.service.alerta;

import com.oroboros.EscalaDeFolga.domain.model.alerta.Alerta;
import com.oroboros.EscalaDeFolga.domain.model.alerta.TipoAlertaEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Gera alertas quando o intervalo entre folgas é menor que o recomendado
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlertaIntervaloMinimo implements IAlertaGenerator {

    private final FolgaRepository folgaRepository;
    private static final int INTERVALO_RECOMENDADO = 5; // dias

    @Override
    public List<Alerta> gerarAlertas(Folga folga) {
        List<Alerta> alertas = new ArrayList<>();

        Optional<LocalDate> ultimaFolga = folgaRepository.findUltimaFolgaAntesDe(
                folga.getColaborador(),
                folga.getDataSolicitada()
        );

        if (ultimaFolga.isEmpty()) {
            return alertas; // Primeira folga, sem alertas
        }

        long diasEntre = ChronoUnit.DAYS.between(ultimaFolga.get(), folga.getDataSolicitada());

        // Gera alerta se intervalo < recomendado
        if (diasEntre > 0 && diasEntre < INTERVALO_RECOMENDADO) {
            String mensagem = String.format(
                    "Intervalo curto entre folgas: %d dias (recomendado: %d+ dias). " +
                            "Última folga: %s. Nova folga: %s.",
                    diasEntre,
                    INTERVALO_RECOMENDADO,
                    ultimaFolga.get(),
                    folga.getDataSolicitada()
            );

            String recomendacao = String.format(
                    "Considere agendar a folga para %s ou posterior.",
                    folga.getDataSolicitada().plusDays(INTERVALO_RECOMENDADO - diasEntre)
            );

            Alerta alerta = Alerta.criar(
                    folga.getEscala(),
                    folga.getColaborador(),
                    folga,
                    TipoAlertaEnum.AVISO_INTERVALO_MINIMO_CURTO,
                    mensagem,
                    recomendacao
            );

            alertas.add(alerta);
            log.info("✅ Alerta de intervalo curto gerado para {}", folga.getColaborador().getNome());
        }

        return alertas;
    }
}
