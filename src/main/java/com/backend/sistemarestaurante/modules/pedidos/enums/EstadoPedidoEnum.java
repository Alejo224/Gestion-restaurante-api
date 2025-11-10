package com.backend.sistemarestaurante.modules.pedidos.enums;

public enum EstadoPedidoEnum {
    BORRADOR,   // al crear desde domicilio
    PENDIENTE,   // Despues de confirmar
    EN_PREPARACION, // En cocina
    LISTO,  // Listo para Entregar/Recoger
    ENTREGADO,  // Completado
    CANCELADO   // Cancelado
}
