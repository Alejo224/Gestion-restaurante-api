package com.backend.sistemarestaurante.modules.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link Usuario}.
 * Proporciona métodos para la gestión de usuarios en la base de datos.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Verifica si existe un usuario con el correo electrónico dado.
     *
     * @param email Correo electrónico a verificar.
     * @return true si el correo ya está registrado, false en caso contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un usuario con el correo electrónico dado.
     *
     * @param telefono a verificar.
     * @return true si el telefono ya está registrado, false en caso contrario.
     */

    boolean existsByTelefono(String telefono);

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email Correo electrónico a buscar.
     * @return Un {@link Optional} que contiene el usuario si se encuentra, o vacío si no existe.
     */
    Optional<Usuario> findByEmail(String email);

    /*
    * Metodo personalizado para buscar el usuario por el email*/
    Optional<Usuario> findUsuarioByEmail(String email);
}
