package com.oroboros.EscalaDeFolga.infrastructure.exeption;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
