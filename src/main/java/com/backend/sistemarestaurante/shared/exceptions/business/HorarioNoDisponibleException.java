package com.backend.sistemarestaurante.shared.exceptions.business;

// Excepciones específicas
public class HorarioNoDisponibleException extends BusinessException {
    public HorarioNoDisponibleException(String message) {
        super(message, "HORARIO_NO_DISPONIBLE");
    }
}
