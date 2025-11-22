package com.oroboros.EscalaDeFolga.app.dto.colaborador;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.CargoEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;

public record ColaboradorResponseDTO(long id, String nome, CargoEnum cargo, TurnoEnum turno, Setor setor) {


}


