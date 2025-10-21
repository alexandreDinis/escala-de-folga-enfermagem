package com.oroboros.EscalaDeFolga.dto.colaborador;

import com.oroboros.EscalaDeFolga.model.CargoEnum;
import com.oroboros.EscalaDeFolga.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.model.colaborador.TurnoEnum;

public record ColaboradorResponseDTO(long id, String nome, CargoEnum cargo, TurnoEnum turno) {

    public ColaboradorResponseDTO(Colaborador colaborador) {
        this(colaborador.getId(), colaborador.getNome(),colaborador.getCargo(),colaborador.getTurno());
    }
}


