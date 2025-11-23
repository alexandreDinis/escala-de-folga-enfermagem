package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusEscalaEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Regra de negócio responsável por validar se uma {@link Escala}
 * pode ser modificada (edição).
 *
 * <h3>📌 Regras atendidas por este validador:</h3>
 * <ul>
 *     <li>A escala só pode ser editada quando está nos estados:
 *         <b>NOVA</b> ou <b>PARCIAL</b>.</li>
 *     <li>Se existir qualquer folga aprovada, a escala não pode ser editada.</li>
 *     <li>Escalas PUBLICADAS ou FECHADAS nunca podem ser editadas.</li>
 * </ul>
 *
 * <h3>📌 Justificativa</h3>
 * A edição de mês, ano ou turno após a geração/validação de folgas
 * poderia causar inconsistências graves na regra 6x1 e na distribuição
 * mensal dos colaboradores.
 *
 * <h3>📌 Exemplos de bloqueio:</h3>
 * <pre>
 * - Escala PUBLICADA → edição proibida
 * - Escala FECHADA → edição proibida
 * - Possui folgas aprovadas → edição proibida
 * </pre>
 */
@Component
@Order(4)
public class ValidaEscalaEditavel implements IEscalaValidator {

    @Override
    public ResultadoValidacao validar(Escala escala) {
        // Valida status da escala
        if (escala.getStatus() == StatusEscalaEnum.PUBLICADA ||
                escala.getStatus() == StatusEscalaEnum.FECHADA) {
            return ResultadoValidacao.erro(String.format(
                    "A escala %d/%d não pode ser modificada pois está no estado %s.",
                    escala.getMes(),
                    escala.getAno(),
                    escala.getStatus()
            ));
        }

        // Valida se existem folgas aprovadas
        // CORREÇÃO: Adiciona verificação de null antes de usar stream()
        if (escala.getFolgas() != null) {
            boolean temAprovadas = escala.getFolgas().stream()
                    .anyMatch(f -> f.getStatus() == StatusFolgaEnum.APROVADA);

            if (temAprovadas) {
                return ResultadoValidacao.erro(
                        "A escala não pode ser alterada pois existem folgas aprovadas."
                );
            }
        }

        return ResultadoValidacao.ok();
    }
}