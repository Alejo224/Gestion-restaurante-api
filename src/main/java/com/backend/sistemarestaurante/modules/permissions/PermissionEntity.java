package com.backend.sistemarestaurante.modules.permissions;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "permissions")
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    // No se puede repetir el nombre de permisos, no se puede actualizar y no puede ser nulo
    @Column(unique = true, nullable = false, updatable = false)
    private String nombre;
}
