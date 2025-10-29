package com.backend.sistemarestaurante.modules.usuarios.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(@NotBlank String email,
                             @NotBlank String password,
                             @NotBlank String nombreCompleto,
                             @NotBlank String confirmPassword,
                             @NotBlank String telefono,
                             @Valid AuthCreateRoleRequest roleRequest) {
}
