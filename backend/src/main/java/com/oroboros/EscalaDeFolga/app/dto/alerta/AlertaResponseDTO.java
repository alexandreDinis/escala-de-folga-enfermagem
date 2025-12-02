package com.oroboros.EscalaDeFolga.app.dto.alerta;

import java.time.LocalDateTime;

/**
 * Response ao buscar alerta específico
 */
public record AlertaResponseDTO(
        Long id,
        Long escalaId,
        Long colaboradorId,
        String colaboradorNome,
        Long folgaId,
        String tipo,
        String severidade,
        String titulo,
        String mensagem,
        String recomendacao,
        String dadosAdicionais,
        LocalDateTime dataCriacao,
        LocalDateTime dataResolucao,
        boolean resolvido,
        boolean lido
) {}
