package com.backend.sistemarestaurante.modules.permissions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    boolean existsByNombre(String nombre);

    List<PermissionEntity> findByNombreIn(List<String> nombres);
}
