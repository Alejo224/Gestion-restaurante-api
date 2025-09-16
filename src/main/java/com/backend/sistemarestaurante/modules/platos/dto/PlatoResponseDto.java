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
public class PlatoResponseDto implements Serializable {
    Long id;
    String nombre;
    String descripcion;
    BigDecimal precio;
    Boolean disponible;
}