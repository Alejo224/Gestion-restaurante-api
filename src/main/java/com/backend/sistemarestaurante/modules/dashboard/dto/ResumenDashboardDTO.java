package com.backend.sistemarestaurante.modules.dashboard.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenDashboardDTO {
    private BigDecimal totalIngresos;
    private Long totalPedidos;
    private Long totalPedidosPagados;
    private Long totalCancelados;
    private BigDecimal ticketPromedio;
    private BigDecimal ventasHoy;
    private Long pedidosHoy;
    private Long reservasHoy;
}
