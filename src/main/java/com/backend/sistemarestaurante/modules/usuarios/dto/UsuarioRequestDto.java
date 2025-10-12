package com.backend.sistemarestaurante.modules.usuarios.dto;

import com.backend.sistemarestaurante.modules.usuarios.validation.ValidPassword;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link com.backend.sistemarestaurante.modules.usuarios.Usuario}
 * Para crear o actualizar un usuario con validaciones (Campos modificlables por el usuario)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDto {
    @NotBlank(message = "El nombre completo es obligatorio")
    String nombreCompleto;

    @Email(message = "El formato del email no es válido")
    @NotBlank(message = "El Email es obligatorio")
    String email;

    @NotEmpty(message = "Contraseña es obligatoria")
    @ValidPassword
    String password;

    @NotEmpty(message = "Debe confirmar la contraseña")
    @Length(message = "La contraseña debe tener al menos 8 caracteres", min = 8)
    String confirmPassword;

    @NotBlank(message = "El numero de telefono es obligatorio")
    String telefono;

    //  Validación de coincidencia
    @AssertTrue(message = "Las contraseñas no coinciden")
    public boolean isPasswordMatch() {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }
}