package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;

public interface IEscalaValidator {

    ResultadoValidacao validar(Escala escala);
}
