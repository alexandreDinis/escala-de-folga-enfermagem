package com.oroboros.EscalaDeFolga.dto.folga;

import com.oroboros.EscalaDeFolga.model.escala.StatusFolgaEnum;

import java.time.LocalDate;

public record FolgaResponseDTO(
        Long id,
        String nomeColaborador,
        LocalDate dataSolicitada,
        String justificativa,
        StatusFolgaEnum status
) {}
