package com.oroboros.EscalaDeFolga.domain.exception;

public class EscalaNotFoundException extends RuntimeException{


    public EscalaNotFoundException(Long id) {
        super(String.format("Escala com o  ID %d não encontrada.", id));
    }

    public EscalaNotFoundException() {
        super("Escala não encontrada.");
    }
}

