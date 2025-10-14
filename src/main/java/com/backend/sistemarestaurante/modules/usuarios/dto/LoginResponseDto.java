package com.backend.sistemarestaurante.modules.usuarios.dto;

import com.backend.sistemarestaurante.modules.Roles.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
public class LoginResponseDto {
    String mensaje;
    String nombreCompleto;
    String email;
    String telefono;
    Set<RoleEntity> roles;

    public LoginResponseDto(String mensaje, String nombreCompleto, String email, String telefono,
                            Set<RoleEntity> roles) {
        this.mensaje = mensaje;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.telefono = telefono;
        this.roles = roles;
    }
}
