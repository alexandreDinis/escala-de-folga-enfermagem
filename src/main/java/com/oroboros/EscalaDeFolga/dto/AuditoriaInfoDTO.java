package com.oroboros.EscalaDeFolga.dto;

public record AuditoriaInfoDTO(
        Long usuarioId,
        String usuarioNome,
        String ipOrigem,
        String userAgent
) {}
