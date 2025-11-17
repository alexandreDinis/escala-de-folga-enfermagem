package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Executor composto responsável por aplicar uma lista de validações sobre a
 * entidade {@link Escala}.
 *
 * <p>
 * Todas as implementações de {@link IEscalaValidator} são injetadas
 * automaticamente pelo Spring e executadas na ordem em que são encontradas
 * no contexto.
 * </p>
 *
 * <p>
 * A execução é interrompida assim que alguma regra retornar um erro,
 * garantindo performance e previsibilidade.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class EscalaValidatorComposite {

    private final List<IEscalaValidator> validators;

    /**
     * Executa sequencialmente todas as validações registradas.
     *
     * @param escala objeto alvo da validação
     * @return primeiro erro encontrado ou OK caso todas as regras passem
     */
    public ResultadoValidacao validar(Escala escala) {
        for (IEscalaValidator validator : validators) {
            var resultado = validator.validar(escala);
            if (!resultado.isValido()) {
                return resultado;
            }
        }
        return ResultadoValidacao.ok();
    }
}
