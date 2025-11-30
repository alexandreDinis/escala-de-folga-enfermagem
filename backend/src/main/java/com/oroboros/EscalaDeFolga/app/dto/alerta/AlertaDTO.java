package com.oroboros.EscalaDeFolga.app.dto.alerta;

import java.time.LocalDateTime;

/**
 * Alerta gerado durante criação de folga
 */
public record AlertaDTO(
        Long id,
        String tipo,
        String severidade,
        String titulo,
        String mensagem,
        String recomendacao,
        String icone,
        String cor,
        LocalDateTime dataCriacao,
        boolean lido
) {}
