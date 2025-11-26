package com.backend.sistemarestaurante.modules.pedidos.dto;

import com.backend.sistemarestaurante.modules.pedidos.enums.TipoServicio;
import lombok.Data;

import java.util.List;
/*
    DTO para la creacion de pedidos
 */

@Data
public class PedidoRequest {

    private TipoServicio tipoServicio;
    // private LocalDateTime horaRecogida; // Para RECOGER_PEDIDO
    private String direccionEntrega; // Para DOMICILIO
    private String telefonoContacto;
    private String notas;
    private List<DetallePedidoRequest> detallePedidoRequestList;
}
