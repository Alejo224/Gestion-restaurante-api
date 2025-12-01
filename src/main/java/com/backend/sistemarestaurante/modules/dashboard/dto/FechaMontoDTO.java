package com.backend.sistemarestaurante.modules.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FechaMontoDTO {
    private LocalDate fecha;
    private BigDecimal monto;
}
