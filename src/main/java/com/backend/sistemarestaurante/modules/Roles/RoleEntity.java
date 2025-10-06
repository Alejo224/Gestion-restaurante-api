package com.backend.sistemarestaurante.modules.Roles;
/*
*  Entidad Rol para los roles de usuario administrador o cliente
* */

import com.backend.sistemarestaurante.modules.permissions.PermissionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/*
* Un Rol puede tener varios permisos y esos permisos se le asignan a varios roles tambien.
* */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_nombre" )
    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;

    // Relacion muchos a muchos con permisos
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionEntity> permissionSet = new HashSet<>();
}
