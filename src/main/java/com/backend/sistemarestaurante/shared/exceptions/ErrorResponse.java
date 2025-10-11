package com.backend.sistemarestaurante.shared.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private Map<String, String> errors;
    private LocalDateTime timestamp;
    private String path;

    public ErrorResponse(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
}
