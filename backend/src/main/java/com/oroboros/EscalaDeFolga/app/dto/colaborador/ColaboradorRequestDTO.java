package com.oroboros.EscalaDeFolga.app.dto.colaborador;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.CargoEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ColaboradorRequestDTO(

        @NotBlank(message = "Nome é obrigatório.")
        String nome,

        @NotNull(message = "Cargo é obrigatório.")
        CargoEnum cargo,

        @NotNull(message = "Turno é obrigatório.")
        TurnoEnum turno,

        @NotNull(message = "Setor é obrigatório.")
        Long setorId)
{
}
