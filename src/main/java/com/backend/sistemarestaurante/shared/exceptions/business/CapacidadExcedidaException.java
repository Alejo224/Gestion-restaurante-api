package com.backend.sistemarestaurante.shared.exceptions.business;

public class CapacidadExcedidaException extends BusinessException {
    public CapacidadExcedidaException(String message) {
        super(message, "CAPACIDAD_EXCEDIDA");
    }
}
