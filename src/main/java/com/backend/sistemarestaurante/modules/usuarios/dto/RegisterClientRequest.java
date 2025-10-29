package com.backend.sistemarestaurante.modules.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterClientRequest(@NotBlank(message = "El email es obligatorio")
                                    @Email(message = "Formato de email inválido")
                                    String email,

                                    @NotBlank(message = "La contraseña es obligatoria")
                                    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
                                    String password,

                                    @NotBlank(message = "Debe confirmar la contraseña")
                                    String confirmPassword,

                                    @NotBlank(message = "El nombre completo es obligatorio")
                                    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
                                    String nombreCompleto,

                                    @NotBlank(message = "El teléfono es obligatorio")
                                    @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 dígitos")
                                    String telefono
) {
}