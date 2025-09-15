package com.backend.sistemarestaurante.modules.clientes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link Cliente}.
 * Proporciona métodos para la gestión de clientes en la base de datos.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Verifica si existe un cliente con el correo electrónico dado.
     *
     * @param email Correo electrónico a verificar.
     * @return true si el correo ya está registrado, false en caso contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email Correo electrónico a buscar.
     * @return Un {@link Optional} que contiene el usuario si se encuentra, o vacío si no existe.
     */
    Optional<Cliente> findByEmail(String email);
}
