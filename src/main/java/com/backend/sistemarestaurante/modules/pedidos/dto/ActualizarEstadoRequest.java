package com.backend.sistemarestaurante.modules.pedidos.dto;

import com.backend.sistemarestaurante.modules.pedidos.enums.EstadoPedidoEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/* Para el administrador*/
@Data
public class ActualizarEstadoRequest {

    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoPedidoEnum nuevoEstado;

    // Solo requerido cuando el estado sea "LISTO" y tipoServicio sea "RECOGER_PEDIDO"
    private LocalDateTime horaRecogida;

    private String notasInternas; // Notas del restaurante
}
