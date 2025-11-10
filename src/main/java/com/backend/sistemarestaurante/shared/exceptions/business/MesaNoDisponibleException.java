package com.backend.sistemarestaurante.shared.exceptions.business;

public class MesaNoDisponibleException extends BusinessException {
    public MesaNoDisponibleException(String message) {
        super(message, "MESA_NO_DISPONIBLE");
    }
}
