package com.backend.sistemarestaurante.modules.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelarPedidoRequest {

    @NotBlank(message = "El motivo de cancelación no puede estar vacío")
    private String motivoCancelacion;
}
