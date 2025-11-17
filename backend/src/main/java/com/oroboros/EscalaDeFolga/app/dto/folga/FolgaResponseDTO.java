package com.oroboros.EscalaDeFolga.app.dto.folga;

import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;

import java.time.LocalDate;

public record FolgaResponseDTO(
        Long id,
        String nomeColaborador,
        LocalDate dataSolicitada,
        String justificativa,
        StatusFolgaEnum status
) {}
