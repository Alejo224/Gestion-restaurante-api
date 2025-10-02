package com.backend.sistemarestaurante.shared.exceptions;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la API REST.
 * <p>
 * Captura excepciones comunes y personalizadas, proporcionando respuestas estructuradas y significativas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Aquí se pueden agregar métodos para manejar diferentes tipos de excepciones
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundExeption(ResourceNotFoundException ex, HttpServletRequest request) {
        // Instancia para el mapeo
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDate.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not found");
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

}
