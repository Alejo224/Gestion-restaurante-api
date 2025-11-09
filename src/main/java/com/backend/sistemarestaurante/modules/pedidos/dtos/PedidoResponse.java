package com.backend.sistemarestaurante.modules.pedidos.dtos;

import com.backend.sistemarestaurante.modules.pedidos.enums.EstadoPedidoEnum;
import com.backend.sistemarestaurante.modules.pedidos.enums.TipoServicio;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponse {
    private Long id;
    private String nombreUsuario;
    private TipoServicio tipoServicio;
    private LocalDateTime fechaPedido;
    private EstadoPedidoEnum estadoPedidoEnum;
    private BigDecimal subtotal, iva, total;
    private String notas;
    private List<DetallePedidoResponse> detalles;
    
}
