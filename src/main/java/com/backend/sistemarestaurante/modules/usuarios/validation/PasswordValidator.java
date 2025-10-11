package com.backend.sistemarestaurante.modules.usuarios.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import javax.naming.Context;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) {
            setCustomMessage(context, "La contraseña es obligatoria");
            return false;
        }

        // Verificar cada criterio
        if (password.length() < 8) {
            setCustomMessage(context, "La contraseña debe tener al menos 8 caracteres");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            setCustomMessage(context, "La contraseña debe contener al menos una letra mayúscula");
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            setCustomMessage(context, "La contraseña debe contener al menos una letra minúscula");
            return false;
        }
        if (!password.matches(".*[0-9].*")) {
            setCustomMessage(context, "La contraseña debe contener al menos un número");
            return false;
        }
        if (!password.matches(".*[!#$%&()*^?/@+=<>_-~{}].*")) {
            setCustomMessage(context, "La contraseña debe contener al menos un carácter especial: ! # $ % & ( ) * ^ ? / @ + = < > _ - ~ { }");
            return false;
        }

        return true;
    }

    private void setCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}