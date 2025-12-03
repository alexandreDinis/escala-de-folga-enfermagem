package com.oroboros.EscalaDeFolga.app.dto.folga;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * DTO para cadastro de histórico de última folga
 * Usado quando admin precisa registrar folgas antigas de colaboradores
 */
public record HistoricoFolgaRequestDTO(

        @NotNull(message = "Colaborador é obrigatório")
        Long colaboradorId,

        @NotNull(message = "Data da última folga é obrigatória")
        @PastOrPresent(message = "Data deve ser passada ou hoje")
        LocalDate dataSolicitada

) {}
