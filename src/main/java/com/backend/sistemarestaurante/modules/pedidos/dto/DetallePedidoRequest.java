package com.backend.sistemarestaurante.modules.pedidos.dto;

import lombok.Data;

@Data
public class DetallePedidoRequest {
    private Long platoId;
    private Integer cantidad;
    private String notas;
    
}
