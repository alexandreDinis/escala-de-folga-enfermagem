package com.oroboros.EscalaDeFolga.app.dto.colaborador;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record UltimaFolgaUpdateDTO(
        @NotNull(message = "Data da última folga é obrigatória")
        @PastOrPresent(message = "Data não pode ser no futuro")
        LocalDate ultimaFolga
) {}
