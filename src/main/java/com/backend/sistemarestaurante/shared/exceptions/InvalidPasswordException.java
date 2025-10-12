package com.backend.sistemarestaurante.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que se lanza cuando una contraseña no cumple con los criterios de seguridad establecidos.
 * <p>
 * Esta excepción devuelve un código de estado HTTP 400 (BAD REQUEST).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPasswordException extends RuntimeException {
  /**
   * Constructor de la excepción.
   *
   * @param message Mensaje de error que describe el problema.
   */
    public InvalidPasswordException(String message) {
        super(message);
    }
}
