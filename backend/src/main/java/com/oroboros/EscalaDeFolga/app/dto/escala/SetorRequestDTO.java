package com.oroboros.EscalaDeFolga.app.dto.escala;

import jakarta.validation.constraints.NotBlank;

public record SetorRequestDTO(
        @NotBlank String nome) {
}
