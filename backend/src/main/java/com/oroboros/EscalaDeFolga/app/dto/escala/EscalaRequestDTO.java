package com.oroboros.EscalaDeFolga.app.dto.escala;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import jakarta.validation.constraints.NotNull;

public record EscalaRequestDTO(
        @NotNull(message = "Mês é obrigatório") Integer mes,
        @NotNull(message = "Ano é obrigatório") Integer ano,
        @NotNull(message = "Folgas permitida é obrigatório") Integer folgasPermitidas,
        @NotNull TurnoEnum turno,
        @NotNull Long setorId
) {}
