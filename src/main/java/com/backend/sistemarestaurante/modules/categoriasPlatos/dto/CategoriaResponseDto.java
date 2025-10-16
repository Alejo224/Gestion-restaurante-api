package com.backend.sistemarestaurante.modules.categoriasPlatos.dto;

import com.backend.sistemarestaurante.modules.categoriasPlatos.CategoriaPlato;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.backend.sistemarestaurante.modules.categoriasPlatos.CategoriaPlato}
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaResponseDto{
    Long id;
    String nombreCategoria;
}