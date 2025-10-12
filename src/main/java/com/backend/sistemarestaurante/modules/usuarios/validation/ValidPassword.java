package com.backend.sistemarestaurante.modules.usuarios.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Contraseña no cumple con los requisitos de seguridad";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

}
