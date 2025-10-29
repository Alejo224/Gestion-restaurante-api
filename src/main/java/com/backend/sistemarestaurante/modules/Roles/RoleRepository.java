package com.backend.sistemarestaurante.modules.Roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    // Buscar el rol por enum
    Optional<RoleEntity> findByRoleEnum (RoleEnum roleEnum);

    // Verificar si existe un rol por enum
    boolean existsByRoleEnum (RoleEnum roleEnum);

    // Traer los roles que solamente existane en la base de datos
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);
}
