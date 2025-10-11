package com.backend.sistemarestaurante.modules.Roles;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    // Buscar el rol por enum
    Optional<RoleEntity> findByRoleEnum (RoleEnum roleEnum);

    // Verificar si existe un rol por enum
    boolean existsByRoleEnum (RoleEnum roleEnum);

}
