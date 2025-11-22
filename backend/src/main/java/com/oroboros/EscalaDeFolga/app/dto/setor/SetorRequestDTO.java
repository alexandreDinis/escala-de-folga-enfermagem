package com.oroboros.EscalaDeFolga.app.dto.setor;

import jakarta.validation.constraints.NotBlank;

public record SetorRequestDTO(
        @NotBlank(message = "Nome do setor é obrigatório.")
        String nome
) {}

