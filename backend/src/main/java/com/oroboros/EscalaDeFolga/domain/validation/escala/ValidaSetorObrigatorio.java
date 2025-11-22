package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Validador que garante que uma escala sempre esteja vinculada a um setor.
 *
 * <p>Antes de permitir a criação de uma nova escala, esta validação verifica
 * se o setor foi informado corretamente. Caso não tenha sido definido, a
 * criação é bloqueada imediatamente.
 *
 * <h3>Por que essa validação é importante?</h3>
 * <ul>
 *     <li>O setor é essencial para identificar a equipe responsável pela escala.</li>
 *     <li>Sem o setor, não é possível aplicar validações adicionais (como duplicidade).</li>
 *     <li>Evita que o usuário crie escalas incompletas ou inconsistentes.</li>
 * </ul>
 *
 * <h3>Exemplo de mensagem gerada</h3>
 * <pre>
 * "A escala deve estar vinculada a um setor. Cadastre um setor antes de criar."
 * </pre>
 *
 * <h3>Ordem de execução</h3>
 * <p>Essa validação deve ser executada <b>antes</b> de outras regras, como a de escala duplicada.</p>
 *
 * @author Alexandre
 */

@Component
@Order(1)
public class ValidaSetorObrigatorio implements IEscalaValidator {

    @Override
    public ResultadoValidacao validar(Escala escala) {
        if (escala.getSetor() == null) {
            return ResultadoValidacao.erro(
                    "A escala deve estar vinculada a um setor. Cadastre um setor antes de criar."
            );
        }
        return ResultadoValidacao.ok();
    }
}
