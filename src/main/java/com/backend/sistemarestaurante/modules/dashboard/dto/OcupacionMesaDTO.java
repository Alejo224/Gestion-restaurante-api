package com.backend.sistemarestaurante.modules.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OcupacionMesaDTO {
    private String mesa;
    private Long reservas;
}
