package com.oroboros.EscalaDeFolga.infrastructure.exeption;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado no sistema.
 *
 * Exemplos de uso:
 * - Escala não encontrada por ID
 * - Setor não encontrado por ID
 * - Colaborador não encontrado por ID
 * - Qualquer entidade que deveria existir mas não foi localizada
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public RecursoNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
