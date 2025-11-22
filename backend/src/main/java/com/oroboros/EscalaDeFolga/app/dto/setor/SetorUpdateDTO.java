package com.oroboros.EscalaDeFolga.app.dto.setor;

import jakarta.validation.constraints.NotNull;

public record SetorUpdateDTO(
        @NotNull(message = "ID do setor é obrigatório para atualização.")
        Long id,

        String nome
) {}
