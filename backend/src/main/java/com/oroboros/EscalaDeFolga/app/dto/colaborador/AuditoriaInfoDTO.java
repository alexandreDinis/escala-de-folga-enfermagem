package com.oroboros.EscalaDeFolga.app.dto.colaborador;

public record AuditoriaInfoDTO(
        Long usuarioId,
        String usuarioNome,
        String ipOrigem,
        String userAgent
) {}
