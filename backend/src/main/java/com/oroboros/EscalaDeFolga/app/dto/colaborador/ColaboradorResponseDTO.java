package com.oroboros.EscalaDeFolga.app.dto.colaborador;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.CargoEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;

public record ColaboradorResponseDTO(
        Long id, 
        String nome, 
        CargoEnum cargo, 
        TurnoEnum turno, 
        Long setorId,
        String setorNome  // ✅ ADICIONAR
) {}
