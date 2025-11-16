package com.oroboros.EscalaDeFolga.app.dto.escala;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import jakarta.validation.constraints.NotNull;

public record EscalaRequestDTO(
        @NotNull(message = "Mês é obrigatório") int mes,
        @NotNull(message = "Ano é obrigatório") int ano,
        @NotNull(message = "Folgas permitida é obrigatório") int folgasPermitidas,
        @NotNull(message = "Turno é obrigatório") TurnoEnum turno
) {}
