package com.oroboros.EscalaDeFolga.app.dto.folga;

import java.time.LocalDate;

/**
 * Request para atualizar folga pendente
 */
public record FolgaUpdateDTO(
        LocalDate dataSolicitada,
        String justificativa
) {}
