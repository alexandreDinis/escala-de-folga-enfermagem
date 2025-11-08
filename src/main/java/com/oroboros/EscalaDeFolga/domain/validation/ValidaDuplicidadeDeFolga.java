package com.oroboros.EscalaDeFolga.domain.validation;

import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidaDuplicidadeDeFolga implements IFolgaValidator {


    private final FolgaRepository folgaRepository;

    @Override
    public boolean validarFolga(Folga folga) {
        return !folgaRepository.existsByColaboradorAndDataSolicitada(
                folga.getColaborador(),
                folga.getDataSolicitada()
        );
    }
}
