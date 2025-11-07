package com.oroboros.EscalaDeFolga.app.dto.colaborador;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.CargoEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;

public record ColaboradorResponseDTO(long id, String nome, CargoEnum cargo, TurnoEnum turno) {

    public ColaboradorResponseDTO(Colaborador colaborador) {
        this(colaborador.getId(), colaborador.getNome(),colaborador.getCargo(),colaborador.getTurno());
    }
}


