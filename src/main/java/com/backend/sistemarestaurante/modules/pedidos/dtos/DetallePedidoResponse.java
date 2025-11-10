package com.backend.sistemarestaurante.modules.pedidos.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetallePedidoResponse {
    private Long id, platoId;
    private String platoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario, subtotal;
    private String notas;
}
