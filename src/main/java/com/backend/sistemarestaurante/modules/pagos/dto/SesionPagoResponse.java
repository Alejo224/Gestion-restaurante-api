package com.backend.sistemarestaurante.modules.pagos.dto;

import lombok.Data;

@Data
public class SesionPagoResponse {
    private String checkoutUrl;
    private String sessionId;
    private Long pedidoId;

    public SesionPagoResponse(String checkoutUrl, String sessionId, Long pedidoId) {
        this.checkoutUrl = checkoutUrl;
        this.sessionId = sessionId;
        this.pedidoId = pedidoId;
    }
}