package com.oroboros.EscalaDeFolga.app.dto.folga;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ValidarDataUltimaFolgaDTO(
        @NotNull(message = "ID do colaborador é obrigatório")
        Long colaboradorId,

        @NotNull(message = "Data é obrigatória")
        LocalDate dataSolicitada,

        @NotNull(message = "ID da escala é obrigatório")
        Long escalaId
) {}
