package com.oroboros.EscalaDeFolga.app.dto.colaborador;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.CargoEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;

public record ColaboradorUpdateDTO(String nome, CargoEnum cargo, TurnoEnum turno, Long setorId) {
}
