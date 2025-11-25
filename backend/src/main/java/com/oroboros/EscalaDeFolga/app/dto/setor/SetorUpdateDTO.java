package com.oroboros.EscalaDeFolga.app.dto.setor;

import jakarta.validation.constraints.NotNull;

public record SetorUpdateDTO(
        @NotNull(message = "nome do setor é obrigatório para atualização.")
        String nome,
        Boolean confirmarReativacao
) {}
