package com.backend.sistemarestaurante.configuration.app;

import com.backend.sistemarestaurante.modules.Roles.RoleEntity;
import com.backend.sistemarestaurante.modules.Roles.RoleEnum;
import com.backend.sistemarestaurante.modules.Roles.RoleRepository;
import com.backend.sistemarestaurante.modules.categoriasPlatos.CategoriaPlato;
import com.backend.sistemarestaurante.modules.categoriasPlatos.CategoriaPlatoRepository;
import com.backend.sistemarestaurante.modules.permissions.PermissionEntity;
import com.backend.sistemarestaurante.modules.permissions.PermissionRepository;
import com.backend.sistemarestaurante.modules.reservas.service.ConfiguracionHorarioService;
import com.backend.sistemarestaurante.modules.usuarios.Usuario;
import com.backend.sistemarestaurante.modules.usuarios.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CategoriaPlatoRepository categoriaPlatoRepository;
    private final ConfiguracionHorarioService configuracionHorarioService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        inicializarPermisos();
        inicializarRoles();
        inicializarCategoriasPlatos();
        inicializarUsuarioAdmin();
        configuracionHorarioService.inicializarHorariosPorDefecto();
    }

    private void inicializarPermisos() {
        if (permissionRepository.count() == 0) {
            try {
                List<PermissionEntity> permisos = Arrays.asList(
                        PermissionEntity.builder().nombre("CREATE").build(),
                        PermissionEntity.builder().nombre("READ").build(),
                        PermissionEntity.builder().nombre("UPDATE").build(),
                        PermissionEntity.builder().nombre("DELETE").build(),
                        PermissionEntity.builder().nombre("PAYMENT_PROCESS").build(),
                        PermissionEntity.builder().nombre("CART_MANAGE").build()
                );
                permissionRepository.saveAll(permisos);
                System.out.println("Permisos inicializados: " + permisos.size());
            } catch (Exception e) {
                System.err.println("Error inicializando permisos: " + e.getMessage());
                throw new RuntimeException("Error en inicialización de permisos", e);
            }
        }
    }

    private void inicializarRoles() {
        if (roleRepository.count() == 0) {
            try {
                // Verificar que existen permisos antes de continuar
                List<PermissionEntity> allPermisos = permissionRepository.findAll();
                if (allPermisos.isEmpty()) {
                    throw new RuntimeException("No se encontraron permisos en la base de datos");
                }

                Set<PermissionEntity> allPermissions = new HashSet<>(allPermisos);

                // Obtener permisos específicos para USER
                List<PermissionEntity> userPermisosList = permissionRepository.findByNombreIn(
                        Arrays.asList("READ", "PAYMENT_PROCESS", "CART_MANAGE"));
                if (userPermisosList.size() != 3) {
                    throw new RuntimeException("No se encontraron todos los permisos para USER");
                }
                Set<PermissionEntity> userPermissions = new HashSet<>(userPermisosList);

                // Obtener permisos específicos para INVITED
                List<PermissionEntity> invitedPermisosList = permissionRepository.findByNombreIn(
                        Arrays.asList("READ"));
                if (invitedPermisosList.size() != 1) {
                    throw new RuntimeException("No se encontró el permiso READ para INVITED");
                }
                Set<PermissionEntity> invitedPermissions = new HashSet<>(invitedPermisosList);

                // Crear y guardar roles
                List<RoleEntity> roles = Arrays.asList(
                        RoleEntity.builder()
                                .roleEnum(RoleEnum.ADMIN)
                                .permissionSet(allPermissions)
                                .build(),
                        RoleEntity.builder()
                                .roleEnum(RoleEnum.USER)
                                .permissionSet(userPermissions)
                                .build(),
                        RoleEntity.builder()
                                .roleEnum(RoleEnum.INVITED)
                                .permissionSet(invitedPermissions)
                                .build()
                );

                roleRepository.saveAll(roles);
                System.out.println("Roles inicializados: " + roles.size());

            } catch (Exception e) {
                System.err.println("Error inicializando roles: " + e.getMessage());
                throw new RuntimeException("Error en inicialización de roles", e);
            }
        }
    }

    private void inicializarCategoriasPlatos() {
        if (categoriaPlatoRepository.count() == 0) {
            List<CategoriaPlato> categorias = Arrays.asList(
                    new CategoriaPlato("Entradas"),
                    new CategoriaPlato("Comida Rapida"),
                    new CategoriaPlato("Platos principales"),
                    new CategoriaPlato("Postres"),
                    new CategoriaPlato("Bebidas")
            );
            categoriaPlatoRepository.saveAll(categorias);
            System.out.println("Categorías de platos inicializadas: " + categorias.size());
        }
    }

    private void inicializarUsuarioAdmin() {
        if (usuarioRepository.findByEmail("Admin007@restaurante.com").isEmpty()) {
            RoleEntity roleAdmin = roleRepository.findByRoleEnum(RoleEnum.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

            Usuario usuarioAdmin = Usuario.builder()
                    .email("Admin007@restaurante.com")
                    .password(passwordEncoder.encode("Univalle@2025")) // ¡IMPORTANTE: Encriptar!
                    .isEnable(true)
                    .isAccountNonExpired(true)
                    .isAccountNonLocked(true)
                    .isCredentialsNonExpired(true)
                    .roles(Set.of(roleAdmin))
                    .telefono("32909212")
                    .nombreCompleto("Administrador")
                    .build();

            usuarioRepository.save(usuarioAdmin);
            System.out.println("Usuario administrador creado");
        }
    }
}