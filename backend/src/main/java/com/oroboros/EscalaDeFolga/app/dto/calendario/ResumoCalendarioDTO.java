package com.oroboros.EscalaDeFolga.app.dto.calendario;

public record ResumoCalendarioDTO(
        int totalColaboradores,
        int totalFolgasAlocadas,
        int totalFolgasDisponiveis,
        int percentualOcupacao,
        int diasAbertos,
        int diasOcupados,
        int diasComAlerta
) {}
