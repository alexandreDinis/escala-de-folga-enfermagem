package com.oroboros.EscalaDeFolga.app.dto.folga;

import java.time.LocalDate;

public record ValidacaoUltimaFolgaResponseDTO(
        boolean valido,
        String mensagem,
        LocalDate dataMinimPermitida,
        int diasTrabalhoMaximo
) {}
