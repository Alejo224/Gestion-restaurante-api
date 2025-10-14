package com.backend.sistemarestaurante.modules.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @Email(message = "El formtado del email no es valido")
    @NotBlank(message = "El email es obligaotrio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
