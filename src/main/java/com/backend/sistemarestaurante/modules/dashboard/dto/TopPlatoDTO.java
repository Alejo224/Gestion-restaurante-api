package com.backend.sistemarestaurante.modules.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopPlatoDTO {
    private String nombrePlato;
    private Long totalVendido;
}
