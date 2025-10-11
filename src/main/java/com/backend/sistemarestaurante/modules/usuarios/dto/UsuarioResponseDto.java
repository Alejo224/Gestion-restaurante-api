package com.backend.sistemarestaurante.modules.usuarios.dto;

import com.backend.sistemarestaurante.modules.Roles.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.backend.sistemarestaurante.modules.usuarios.Usuario}
 * Respuesta al crear o consultar un usuario
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDto {
    Long id;
    String nombreCompleto;
    String email;
    String telefono;
    Set<RoleEntity> roles;

    // NO incluir: password, campos de security internos
}