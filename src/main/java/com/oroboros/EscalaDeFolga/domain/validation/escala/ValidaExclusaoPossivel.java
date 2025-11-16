package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusEscalaEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import org.springframework.stereotype.Component;

/**
 * Validação responsável por garantir que uma {@link Escala}
 * só possa ser excluída quando não houver impacto em dados operacionais.
 *
 * <h3>📌 Regras aplicadas:</h3>
 * <ul>
 *     <li>A escala não pode ser excluída se seu status for PUBLICADA ou FECHADA.</li>
 *     <li>Se houver folgas aprovadas, a exclusão é bloqueada.</li>
 *     <li>Se existirem registros de trabalho (EscalaColaborador), a exclusão é bloqueada.</li>
 * </ul>
 *
 * <h3>📌 Justificativa</h3>
 * A exclusão de uma escala utilizada poderia causar perda significativa
 * de rastreabilidade e inconsistência operacional.
 *
 * <h3>📌 Exemplo de bloqueio:</h3>
 * <pre>
 * - Escala PUBLICADA → não pode excluir
 * - Possui folgas aprovadas → não pode excluir
 * - Possui EscalaColaborador → não pode excluir
 * </pre>
 */
@Component
public class ValidaExclusaoPossivel implements IEscalaValidator {

    @Override
    public ResultadoValidacao validar(Escala escala) {
        // Valida status da escala
        if (escala.getStatus() == StatusEscalaEnum.PUBLICADA ||
                escala.getStatus() == StatusEscalaEnum.FECHADA) {
            return ResultadoValidacao.erro(
                    "Esta escala não pode ser excluída pois já está publicada ou fechada."
            );
        }

        // Valida se existem folgas aprovadas
        // CORREÇÃO: Adiciona verificação de null antes de usar stream()
        if (escala.getFolgas() != null) {
            boolean temAprovadas = escala.getFolgas().stream()
                    .anyMatch(f -> f.getStatus() == StatusFolgaEnum.APROVADA);

            if (temAprovadas) {
                return ResultadoValidacao.erro(
                        "Não é possível excluir esta escala pois existem folgas aprovadas associadas."
                );
            }
        }

        // Valida se existem registros de trabalho
        // CORREÇÃO: Adiciona verificação de null antes de verificar isEmpty()
        if (escala.getRegistros() != null && !escala.getRegistros().isEmpty()) {
            return ResultadoValidacao.erro(
                    "Não é possível excluir a escala pois existem registros de trabalho vinculados."
            );
        }

        return ResultadoValidacao.ok();
    }
}