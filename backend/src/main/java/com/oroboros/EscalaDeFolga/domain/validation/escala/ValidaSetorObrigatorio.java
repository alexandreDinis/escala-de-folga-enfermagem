package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import com.oroboros.EscalaDeFolga.infrastructure.repository.SetorRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Order(1)
public class ValidaSetorObrigatorio implements IEscalaValidator {

    private final SetorRepository setorRepository;

    @Override
    public ResultadoValidacao validar(Escala escala) {

        // Valida se o setor foi informado
        if (escala.getSetor() == null || escala.getSetor().getId() == null) {
            return ResultadoValidacao.erro(
                    "A escala deve estar vinculada a um setor válido. Informe o ID do setor."
            );
        }

        // Valida se o ID é válido
        if (escala.getSetor().getId() <= 0) {
            return ResultadoValidacao.erro(
                    "O ID do setor deve ser um número positivo maior que zero."
            );
        }

        // ⚠️ IMPORTANTE: Verifica se o setor realmente existe no banco
        boolean setorExiste = setorRepository.existsById(escala.getSetor().getId());

        if (!setorExiste) {
            return ResultadoValidacao.erro(
                    String.format("Setor não encontrado com ID: %d. Cadastre o setor antes de criar a escala.",
                            escala.getSetor().getId())
            );
        }

        return ResultadoValidacao.ok();
    }
}
