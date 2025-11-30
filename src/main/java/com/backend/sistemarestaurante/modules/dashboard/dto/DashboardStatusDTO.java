package com.backend.sistemarestaurante.modules.dashboard.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class DashboardStatusDTO {
    private Long reservasHoy;
    private Long reservasConfirmadasHoy; // Nuevo campo
    private String mesasOcupadas;
    private BigDecimal ingresosHoy;
    private Long platosActivos;
    private Long totalPedidosHoy;
}