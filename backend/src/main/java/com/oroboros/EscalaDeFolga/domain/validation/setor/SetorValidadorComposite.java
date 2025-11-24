package com.oroboros.EscalaDeFolga.domain.validation.setor;

import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
        * Executor composto responsável por aplicar uma lista de validações sobre a
 * entidade {@link Setor
}.
        *
        * <p>
 * Todas as implementações de {@link ISetorValidator} são injetadas
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
public class SetorValidadorComposite {

    private final List<ISetorValidator> validators;

    /**
     * Executa sequencialmente todas as validações registradas.
     *
     * @param setor objeto alvo da validação
     * @return primeiro erro encontrado ou OK caso todas as regras passem
     */
    public ResultadoValidacao validar(Setor setor){
        for (ISetorValidator validator : validators){
            var resultado = validator.validar(setor);
            if (!resultado.isValido()){
                return resultado;
            }
        }
        return ResultadoValidacao.ok();
    }
}
