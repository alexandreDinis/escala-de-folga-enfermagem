package com.oroboros.EscalaDeFolga.dto.colaborador;

import com.oroboros.EscalaDeFolga.model.CargoEnum;
import com.oroboros.EscalaDeFolga.model.colaborador.TurnoEnum;

public record ColaboradorUpdateDTO(String nome, CargoEnum cargo, TurnoEnum turno) {
}
