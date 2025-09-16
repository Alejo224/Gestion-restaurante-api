package com.backend.sistemarestaurante.modules.categoriasPlatos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.backend.sistemarestaurante.modules.categoriasPlatos.CategoriaPlato}
 */
@Value
public class CategoriaPlatoDto implements Serializable {
    @NotBlank(message = "El nombre de la categoria es obligatorio")
    String nombreCategoria;
}