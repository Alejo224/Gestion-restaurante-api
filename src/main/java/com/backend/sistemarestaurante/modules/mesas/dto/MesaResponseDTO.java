package com.backend.sistemarestaurante.modules.mesas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.backend.sistemarestaurante.modules.mesas.Mesa}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MesaResponseDTO {
    private Long id;
    private String nombreMesa;
    private boolean estado;
    private Integer capacidad;
}