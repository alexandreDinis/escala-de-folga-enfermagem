package com.oroboros.EscalaDeFolga.dto;

import com.oroboros.EscalaDeFolga.model.CargoEnum;
import com.oroboros.EscalaDeFolga.model.TurnoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ColaboradorInputDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotNull(message = "Cargo é obrigatório")
        CargoEnum cargo,

        TurnoEnum turno) {
}
