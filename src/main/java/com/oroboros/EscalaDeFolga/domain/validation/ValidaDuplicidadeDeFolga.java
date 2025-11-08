package com.oroboros.EscalaDeFolga.domain.validation;

import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Regra de validação responsável por verificar se já existe uma folga cadastrada
 * para o mesmo colaborador em uma data específica.
 *
 * <p>Esta validação garante que não sejam criadas solicitações duplicadas de folga
 * para o mesmo colaborador e dia. É aplicada antes de persistir uma nova folga no banco,
 * impedindo sobreposição de registros e garantindo integridade na escala.</p>
 *
 * <p>Implementa a interface {@link IFolgaValidator} para padronizar as validações de regras
 * relacionadas a folgas dentro do sistema.</p>
 *
 * @author Alexandre Dinis
 * @see IFolgaValidator
 * @see FolgaRepository
 */
@Component
@RequiredArgsConstructor
public class ValidaDuplicidadeDeFolga implements IFolgaValidator {


    private final FolgaRepository folgaRepository;

    /**
     * Verifica se o colaborador já possui uma folga cadastrada na mesma data.
     *
     * <p>O método consulta o {@link FolgaRepository} e retorna {@code true}
     * se a folga for válida (ou seja, ainda não existir uma folga no mesmo dia
     * para o colaborador informado). Caso já exista uma folga, retorna {@code false}.</p>
     *
     * @param folga objeto {@link Folga} contendo o colaborador e a data solicitada
     * @return {@code true} se não houver duplicidade de folga, {@code false} caso já exista
     */
    @Override
    public boolean validarFolga(Folga folga) {
        return !folgaRepository.existsByColaboradorAndDataSolicitada(
                folga.getColaborador(),
                folga.getDataSolicitada()
        );
    }
}
