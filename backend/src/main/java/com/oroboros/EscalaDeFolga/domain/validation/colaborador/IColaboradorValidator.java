package com.oroboros.EscalaDeFolga.domain.validation.colaborador;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;

public interface IColaboradorValidator {

    ResultadoValidacao validar(Colaborador colaborador);
}
