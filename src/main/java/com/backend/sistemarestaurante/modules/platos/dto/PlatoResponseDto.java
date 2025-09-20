package com.backend.sistemarestaurante.modules.platos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.backend.sistemarestaurante.modules.platos.Plato}
 * Respuesta para el cliente
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatoResponseDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Boolean disponible;
    private String categoriaId;
}