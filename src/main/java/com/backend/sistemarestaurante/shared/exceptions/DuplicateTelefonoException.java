package com.backend.sistemarestaurante.shared.exceptions;

public class DuplicateTelefonoException extends RuntimeException {
    public DuplicateTelefonoException(String message) {
        super(message);
    }
}
