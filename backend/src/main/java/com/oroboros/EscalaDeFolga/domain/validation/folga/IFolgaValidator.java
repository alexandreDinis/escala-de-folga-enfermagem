package com.oroboros.EscalaDeFolga.domain.validation.folga;

import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;

public interface IFolgaValidator {

    ResultadoValidacao validar(Folga folga);

}
