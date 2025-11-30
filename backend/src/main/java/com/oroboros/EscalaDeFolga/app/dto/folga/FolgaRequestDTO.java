package com.oroboros.EscalaDeFolga.app.dto.folga;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FolgaRequestDTO(

        @NotNull(message = "Escala é obrigatória")
        Long colaboradorId,

        @NotNull(message = "Colaborador é obrigatório")
        Long escalaId,

        @NotNull(message = "Data é obrigatória")
        LocalDate dataSolicitada,

        String justificativa
) {}
