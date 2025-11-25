package com.oroboros.EscalaDeFolga.domain.exception;

public class BusinessException extends RuntimeException {

    // Construtor original - recebe mensagem customizada
    public BusinessException(String message) {
        super(message);
    }

    // Construtor para "entidade não encontrada" - gera mensagem automaticamente
    public BusinessException(String nomeEntidade, Long id) {
        super(String.format("%s com ID %d não encontrado.", nomeEntidade, id));
    }

    // Sobrecarga para aceitar Object como ID (caso use UUID ou String)
    public BusinessException(String nomeEntidade, Object id) {
        super(String.format("%s com ID %s não encontrado.", nomeEntidade, id));
    }
}
