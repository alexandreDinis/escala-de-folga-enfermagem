package com.oroboros.EscalaDeFolga.domain.validation;

import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;

public interface IFolgaValidator {

    ResultadoValidacao validar(Folga folga);

}
