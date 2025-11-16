package com.oroboros.EscalaDeFolga.domain.validation.folga;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultadoValidacao {

    private final boolean valido;
    private final String mensagem;

    public static ResultadoValidacao ok() {
        return new ResultadoValidacao(true, "Folga válida.");
    }

    public static ResultadoValidacao erro(String mensagem) {
        return new ResultadoValidacao(false, mensagem);
    }
}
