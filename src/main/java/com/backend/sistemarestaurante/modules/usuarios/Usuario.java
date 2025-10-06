package com.backend.sistemarestaurante.modules.usuarios;

import com.backend.sistemarestaurante.modules.Roles.RoleEntity;
import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombreCompleto")
    private String nombreCompleto;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private String password;

    @Column(name = "telefono", unique = true, nullable = false)
    private String telefono;

    // Variables que se necesitan para el sprint security
    @Column(name= "is_enable")
    private boolean isEnable;

    @Column(name= "is_AccountNonExpired")
    private boolean isAccountNonExpired;

    @Column(name= "is_AccountNonLocked")
    private boolean isAccountNonLocked;

    @Column(name= "is_CredentialsNonExpired")
    private boolean isCredentialsNonExpired;

    // Relacion unidereccional con la tabla roles
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity>  roles = new HashSet<>();

}
