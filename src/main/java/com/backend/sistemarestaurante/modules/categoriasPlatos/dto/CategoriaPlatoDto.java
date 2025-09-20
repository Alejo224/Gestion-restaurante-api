package com.backend.sistemarestaurante.modules.categoriasPlatos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.backend.sistemarestaurante.modules.categoriasPlatos.CategoriaPlato}
 * Para crear o actualizar una categoría de plato
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaPlatoDto {
    @NotBlank(message = "El nombre de la categoria es obligatorio")
    String nombreCategoria;
}