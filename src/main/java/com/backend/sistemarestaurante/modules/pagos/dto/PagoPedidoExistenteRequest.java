package com.backend.sistemarestaurante.modules.pagos.dto;

import lombok.Data;

@Data
public class PagoPedidoExistenteRequest {
    private Long pedidoId;
    private String customerEmail;
}
