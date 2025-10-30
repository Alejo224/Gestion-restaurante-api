package com.backend.sistemarestaurante.modules.mesas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.backend.sistemarestaurante.modules.mesas.Mesa}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MesaResponseDTO {
    Long id;
    String nombreMesa;
    boolean estado;
    Long capacidad;
}