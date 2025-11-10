package com.backend.sistemarestaurante.shared.exceptions;


import com.backend.sistemarestaurante.shared.exceptions.business.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

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

    // Manejar errores de validación de DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(
                "Error de validación en los datos enviados",
                errors
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmailException(DuplicateEmailException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateTelefonoException.class)
    public ResponseEntity<String> handleDuplicateTelefonoException(DuplicateTelefonoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /*
        Manejo de excepciones del negocio (disponibilidad, capacidad, etc)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex,
                                                                 WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorCode", ex.getErrorCode());

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), errors);
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HorarioNoDisponibleException.class)
    public ResponseEntity<ErrorResponse> handleHorarioNoDisponible(HorarioNoDisponibleException ex,
                                                                   WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorCode", ex.getErrorCode());
        errors.put("type", "SCHEDULE_VALIDATION");

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), errors);
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MesaNoDisponibleException.class)
    public ResponseEntity<ErrorResponse> handleMesaNoDisponible(MesaNoDisponibleException ex,
                                                                WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorCode", ex.getErrorCode());
        errors.put("type", "TABLE_UNAVAILABLE");

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), errors);
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CapacidadExcedidaException.class)
    public ResponseEntity<ErrorResponse> handleCapacidadExcedida(CapacidadExcedidaException ex,
                                                                 WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorCode", ex.getErrorCode());
        errors.put("type", "CAPACITY_EXCEEDED");

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), errors);
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictoReservaException.class)
    public ResponseEntity<ErrorResponse> handleConflictoReserva(ConflictoReservaException ex,
                                                                WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorCode", ex.getErrorCode());
        errors.put("type", "RESERVATION_CONFLICT");

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), errors);
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
