package com.backend.sistemarestaurante.modules.dashboard.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class DashboardStatsDTO {
    private Long reservasHoy;
    private String mesasOcupadas;
    private BigDecimal ingresosHoy;
    private Long platosActivos;
    private Long totalPedidosHoy;
}