package com.oroboros.EscalaDeFolga.dto;

import com.oroboros.EscalaDeFolga.model.CargoEnum;
import com.oroboros.EscalaDeFolga.model.TurnoEnum;

public record ColaboradorUpdateDTO(String nome, CargoEnum cargo, TurnoEnum turno) {
}
