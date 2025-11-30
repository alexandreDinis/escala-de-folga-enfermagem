package com.oroboros.EscalaDeFolga.domain.service.alerta;

import com.oroboros.EscalaDeFolga.domain.model.alerta.Alerta;
import com.oroboros.EscalaDeFolga.domain.model.alerta.TipoAlertaEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.infrastructure.repository.ColaboradorRepository;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Gera alertas quando há desequilíbrio na distribuição de folgas por turno
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlertaDesequilibrioTurno implements IAlertaGenerator {

    private final FolgaRepository folgaRepository;
    private final ColaboradorRepository colaboradorRepository;
    private static final double LIMITE_COBERTURA_MINIMA = 0.60; // 60%

    @Override
    public List<Alerta> gerarAlertas(Folga folga) {
        List<Alerta> alertas = new ArrayList<>();

        var escala = folga.getEscala();
        var turno = escala.getTurno();
        var dataSolicitada = folga.getDataSolicitada();

        // Conta folgas no mesmo dia/turno (incluindo a atual)
        long folgasNoDia = folgaRepository.countByEscalaAndDataSolicitadaAndStatusIn(
                escala,
                dataSolicitada,
                List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA)
        );

        // Total de colaboradores no turno/setor
        long totalColaboradores = colaboradorRepository.countBySetorAndTurno(
                escala.getSetor(),
                turno
        );

        if (totalColaboradores > 0) {
            double percentualEmFolga = (double) folgasNoDia / totalColaboradores;
            double percentualCobertura = 1 - percentualEmFolga;

            if (percentualCobertura < LIMITE_COBERTURA_MINIMA) {
                String mensagem = String.format(
                        "Desequilíbrio de cobertura em %s: " +
                                "%d de %d colaboradores em folga. " +
                                "Cobertura do turno: %.0f%% (mínimo recomendado: %.0f%%)",
                        dataSolicitada,
                        folgasNoDia,
                        totalColaboradores,
                        percentualCobertura * 100,
                        LIMITE_COBERTURA_MINIMA * 100
                );

                String recomendacao = String.format(
                        "Considere redistribuir folgas. " +
                                "Máximo recomendado para este dia: %d folgas simultâneas.",
                        (long) Math.floor(totalColaboradores * (1 - LIMITE_COBERTURA_MINIMA))
                );

                Alerta alerta = Alerta.criar(
                        escala,
                        folga.getColaborador(),
                        folga,
                        TipoAlertaEnum.AVISO_DESEQUILIBRIO_TURNO,
                        mensagem,
                        recomendacao
                );

                alertas.add(alerta);
                log.warn("✅ Alerta de desequilíbrio gerado para turno {}", turno.name());
            }
        }

        return alertas;
    }
}
