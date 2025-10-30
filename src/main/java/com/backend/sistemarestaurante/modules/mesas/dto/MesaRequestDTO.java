package com.backend.sistemarestaurante.modules.mesas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MesaRequestDTO {

    @NotBlank(message = "La mesa debe tener un nombre")
    String nombreMesa;

    boolean estado;

    Long capacidad;
}
