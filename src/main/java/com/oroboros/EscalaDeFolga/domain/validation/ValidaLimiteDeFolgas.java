package com.oroboros.EscalaDeFolga.domain.validation;

import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
        * Regra de validação responsável por verificar o limite máximo de folgas permitidas
 * para um colaborador em determinada escala.
        *
        * <p>Implementa a interface {@link IFolgaValidator} e é executada antes de registrar
 * uma nova solicitação de folga, garantindo que o colaborador não ultrapasse o limite
 * mensal configurado pelo administrador.</p>
        */
@Component
@RequiredArgsConstructor
public class ValidaLimiteDeFolgas implements IFolgaValidator{

    private final FolgaRepository folgaRepository;


    /**
     * Valida se o colaborador ainda pode solicitar uma nova folga dentro da escala atual.
     *
     * <p>Esta regra impede que um colaborador ultrapasse o limite de folgas permitido
     * definido pelo administrador da escala. O método consulta o repositório de folgas
     * e conta quantas solicitações (pendentes ou aprovadas) o colaborador já possui
     * para a escala informada.</p>
     *
     * @param folga objeto {@link Folga} contendo o colaborador e a escala vinculada
     * @return {@code true} se o colaborador ainda estiver dentro do limite de folgas permitidas,
     *         {@code false} caso contrário
     */

    @Override
    public boolean validarFolga(Folga folga) {

        Escala escala = folga.getEscala();

        long totalFolga = folgaRepository.countByColaboradorAndEscalaAndStatusIn(
                folga.getColaborador(),
                escala,
                List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA));

        return totalFolga < escala.getFolgasPermitidas();
    }
}
