package com.oroboros.EscalaDeFolga.app.dto.folga;

import java.time.LocalDate;

public record FolgaRequestDTO(
        Long colaboradorId,
        Long escalaId,
        LocalDate dataSolicitada,
        String justificativa
) {}
