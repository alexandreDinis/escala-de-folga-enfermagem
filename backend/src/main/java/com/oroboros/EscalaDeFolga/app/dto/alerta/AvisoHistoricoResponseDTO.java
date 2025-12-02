package com.oroboros.EscalaDeFolga.app.dto.alerta;

import java.util.List;

public record AvisoHistoricoResponseDTO(
        boolean temAvisos,
        String mensagem,
        List<ColaboradorSemHistoricoDTO> colaboradores
) {}
