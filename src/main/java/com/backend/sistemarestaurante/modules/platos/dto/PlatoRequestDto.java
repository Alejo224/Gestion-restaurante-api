package com.backend.sistemarestaurante.modules.platos.dto;

import com.backend.sistemarestaurante.modules.categoriasPlatos.dto.CategoriaPlatoDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for {@link com.backend.sistemarestaurante.modules.platos.Plato}
 * Para crear o actualizar un plato con validaciones
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatoRequestDto {
    @NotBlank(message = "El plato debe tener un nombre")
    private String nombre;

    private String descripcion;

    @NotNull(message = "Debe tener un precio")
    @Min(message = "Minimo del precio $2000", value = 2000)
    @Positive(message = "El precio debe ser positvo")
    private BigDecimal precio;

    private Boolean disponible = true;

    @NotNull(message = "Debe indicar la categoria")
    private Long categoriaId;
}