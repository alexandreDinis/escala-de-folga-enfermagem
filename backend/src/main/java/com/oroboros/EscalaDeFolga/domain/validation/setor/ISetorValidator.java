package com.oroboros.EscalaDeFolga.domain.validation.setor;

import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;

public interface ISetorValidator {

    ResultadoValidacao validar(Setor setor);
}
