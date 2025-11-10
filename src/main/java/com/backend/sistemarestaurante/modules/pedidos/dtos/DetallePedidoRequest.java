package com.backend.sistemarestaurante.modules.pedidos.dtos;

import com.backend.sistemarestaurante.modules.pedidos.enums.EstadoPedidoEnum;
import com.backend.sistemarestaurante.modules.pedidos.enums.TipoServicio;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DetallePedidoRequest {
    private Long platoId;
    private Integer cantidad;
    private String notas;
    
}
