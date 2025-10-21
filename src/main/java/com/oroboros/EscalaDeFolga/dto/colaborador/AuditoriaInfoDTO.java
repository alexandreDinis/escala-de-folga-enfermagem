package com.oroboros.EscalaDeFolga.dto.colaborador;

public record AuditoriaInfoDTO(
        Long usuarioId,
        String usuarioNome,
        String ipOrigem,
        String userAgent
) {}
