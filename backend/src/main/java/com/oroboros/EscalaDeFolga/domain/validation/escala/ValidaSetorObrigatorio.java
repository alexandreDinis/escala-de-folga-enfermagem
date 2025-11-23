package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import com.oroboros.EscalaDeFolga.infrastructure.repository.SetorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Validador que garante que a escala está vinculada a um setor válido e existente.
 *
 * <h3>Regras validadas:</h3>
 * <ul>
 *     <li>O setor não pode ser nulo</li>
 *     <li>O ID do setor não pode ser nulo ou menor/igual a zero</li>
 *     <li>O setor deve existir cadastrado no banco de dados</li>
 * </ul>
 *
 * <p>Esta validação deve ser a PRIMEIRA a ser executada (Order = 1),
 * pois outras validações dependem do setor existir.
 *
 * <p><b>⚠️ IMPORTANTE para uso com EntityManager.getReference():</b><br>
 * Este validador é CRÍTICO quando o mapper usa referências lazy.
 * Sem esta validação, erros de FK só ocorreriam no momento do save(),
 * gerando exceções menos claras para o usuário.
 *
 * @author Alexandre
 */
@Component
@Order(1)
@RequiredArgsConstructor
public class ValidaSetorObrigatorio implements IEscalaValidator {

    private final SetorRepository setorRepository;

    @Override
    public ResultadoValidacao validar(Escala escala) {

        // Valida se o setor foi informado
        if (escala.getSetor() == null) {
            return ResultadoValidacao.erro(
                    "A escala deve estar vinculada a um setor válido. Informe o ID do setor."
            );
        }

        Long setorId;
        try {
            // Tenta obter o ID (pode lançar exceção se for proxy não inicializado de setor inexistente)
            setorId = escala.getSetor().getId();
        } catch (EntityNotFoundException e) {
            return ResultadoValidacao.erro(
                    "O setor informado não existe no sistema. Cadastre o setor antes de criar a escala."
            );
        }

        // Valida se o ID é válido
        if (setorId == null || setorId <= 0) {
            return ResultadoValidacao.erro(
                    "O ID do setor deve ser um número positivo maior que zero."
            );
        }

        // ⚠️ VALIDAÇÃO CRÍTICA: Verifica se o setor realmente existe no banco
        // Esta verificação é ESSENCIAL quando usando EntityManager.getReference()
        // pois o proxy não dispara erro até tentar persistir
        boolean setorExiste = setorRepository.existsById(setorId);

        if (!setorExiste) {
            return ResultadoValidacao.erro(
                    String.format("Setor não encontrado com ID: %d. Cadastre o setor antes de criar a escala.",
                            setorId)
            );
        }

        return ResultadoValidacao.ok();
    }
}