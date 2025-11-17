package com.oroboros.EscalaDeFolga.app.dto.colaborador;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.CargoEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ColaboradorInputDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotNull(message = "Cargo é obrigatório")
        CargoEnum cargo,

        TurnoEnum turno) {
}
