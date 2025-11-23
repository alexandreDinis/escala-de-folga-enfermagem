package com.oroboros.EscalaDeFolga.infrastructure.exeption;

/**
 * Exceção lançada quando uma regra de validação de negócio é violada.
 * Utilizada especialmente pelos validadores da camada de domínio.
 *
 * Exemplos de uso:
 * - Escala duplicada
 * - Setor inválido ou inexistente
 * - Histórico incompleto de folgas
 * - Validações de integridade de dados de negócio
 */
public class ValidacaoException extends RuntimeException {

    public ValidacaoException(String mensagem) {
        super(mensagem);
    }

    public ValidacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}



