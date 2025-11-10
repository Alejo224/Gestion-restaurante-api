package com.backend.sistemarestaurante.shared.exceptions.business;

public class ConflictoReservaException extends BusinessException {
    public ConflictoReservaException(String message) {
        super(message, "CONFLICTO_RESERVA");
    }
}
