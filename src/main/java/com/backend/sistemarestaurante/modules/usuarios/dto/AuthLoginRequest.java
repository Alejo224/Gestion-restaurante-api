package com.backend.sistemarestaurante.modules.usuarios.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String email, @NotBlank String password) {
}
