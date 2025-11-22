package com.oroboros.EscalaDeFolga.domain.exception;

public class SetorNotFoundExeption extends RuntimeException{

    public SetorNotFoundExeption(){
        super("Setor não cadastrado.");
    }
}
