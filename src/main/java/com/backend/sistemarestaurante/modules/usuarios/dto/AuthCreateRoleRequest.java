package com.backend.sistemarestaurante.modules.usuarios.dto;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;

// enviar la lista de los roles
@Validated
public record AuthCreateRoleRequest(
        @Size(max = 3, message = "El usurio no puede tener mas de 3 roles") List<String> roleListName) {

}
