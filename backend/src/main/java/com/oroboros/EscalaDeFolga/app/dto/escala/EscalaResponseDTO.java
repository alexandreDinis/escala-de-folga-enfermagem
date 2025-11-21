package com.oroboros.EscalaDeFolga.app.dto.escala;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;

public record EscalaResponseDTO(
        Long id,
        Integer mes,
        Integer ano,
        Integer folgasPermitidas,
        TurnoEnum turno,
        Long setorId
) {}
