package com.backend.sistemarestaurante.modules.usuarios.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) {
            setCustomMessage(context, "La contraseña es obligatoria");
            return false;
        }

        // Verificar cada criterio individualmente
        if (password.length() < 8) {
            setCustomMessage(context, "La contraseña debe tener al menos 8 caracteres");
            return false;
        }
        if (!containsUppercase(password)) {
            setCustomMessage(context, "La contraseña debe contener al menos una letra mayúscula");
            return false;
        }
        if (!containsLowercase(password)) {
            setCustomMessage(context, "La contraseña debe contener al menos una letra minúscula");
            return false;
        }
        if (!containsDigit(password)) {
            setCustomMessage(context, "La contraseña debe contener al menos un número");
            return false;
        }
        if (!containsSpecialCharacter(password)) {
            setCustomMessage(context, "La contraseña debe contener al menos un carácter especial: ! @ # $ % ^ & * ( ) _ + - = [ ] { } ; ' : \" | , . < > / ?");
            return false;
        }

        return true;
    }

    // Verificar si almenos hay una mayuscula
    private boolean containsUppercase(String password) {
        return password.chars().anyMatch(Character::isUpperCase);
    }

    // Verificar si almenos hay una minuscula
    private boolean containsLowercase(String password) {
        return password.chars().anyMatch(Character::isLowerCase);
    }

    // Verificar si almenos hay un digito
    private boolean containsDigit(String password) {
        return password.chars().anyMatch(Character::isDigit);
    }

    // Verificar si almenos hay un caracter especial
    private boolean containsSpecialCharacter(String password) {
        String specialChars = "!@#$%^&*()_+-=[]{};':\"|,.<>/?";
        return password.chars().anyMatch(ch -> specialChars.indexOf(ch) >= 0);
    }

    private void setCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}