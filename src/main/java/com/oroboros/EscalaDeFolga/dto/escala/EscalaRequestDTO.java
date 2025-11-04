package com.oroboros.EscalaDeFolga.dto.escala;

import jakarta.validation.constraints.NotNull;

public record EscalaRequestDTO(
        @NotNull(message = "Mês é obrigatório") int mes,
        @NotNull(message = "Ano é obrigatório") int ano
) {}
