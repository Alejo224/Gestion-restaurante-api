package com.backend.sistemarestaurante;

import com.backend.sistemarestaurante.modules.Roles.RoleEntity;
import com.backend.sistemarestaurante.modules.Roles.RoleEnum;
import com.backend.sistemarestaurante.modules.permissions.PermissionEntity;
import com.backend.sistemarestaurante.modules.usuarios.Usuario;
import com.backend.sistemarestaurante.modules.usuarios.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SistemaRestauranteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaRestauranteApplication.class, args);
    }

    /*
    * Metodo que se va a ejecutar inmediatamente se levanta la aplicacion,
    * Se va a ejecutar codigo para poblar nuestras tablas (guardar los registros)
    * */

    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepository) {
        return args -> {
            /* CREATE PERMISSIONS*/
            PermissionEntity createPermission = PermissionEntity.builder()
                    .nombre("CREATE")
                    .build();

            PermissionEntity readPermission = PermissionEntity.builder()
                    .nombre("READ")
                    .build();

            PermissionEntity updatePermission = PermissionEntity.builder()
                    .nombre("UPDATE")
                    .build();

            PermissionEntity deletePermission = PermissionEntity.builder()
                    .nombre("DELETE")
                    .build();

            /* PERMISOS ESPECÍFICOS CLAVE */
            PermissionEntity paymentPermission = PermissionEntity.builder()
                    .nombre("PAYMENT_PROCESS")  // Para procesar pagos
                    .build();

            PermissionEntity cartPermission = PermissionEntity.builder()
                    .nombre("CART_MANAGE")      // Para gestionar carrito
                    .build();

            /* ROL Admin - Todo */
            RoleEntity roleAdmin = RoleEntity.builder()
                    .roleEnum(RoleEnum.ADMIN)
                    .permissionSet(Set.of(createPermission, readPermission, deletePermission,
                            updatePermission, paymentPermission, cartPermission))
                    .build();

            /* ROL User - Lectura + Carrito + Pagos */
            RoleEntity roleUser = RoleEntity.builder()
                    .roleEnum(RoleEnum.USER)
                    .permissionSet(Set.of(readPermission, paymentPermission, cartPermission))
                    .build();

            /* ROL Invited - Lectura*/
            RoleEntity roleInvited = RoleEntity.builder()
                    .roleEnum(RoleEnum.INVITED)
                    .permissionSet(Set.of(readPermission))
                    .build();

            /* Create USERS*/
            Usuario usuarioAdmin = Usuario.builder()
                    .email("admin@gmail.com")
                    .password("1234")
                    .isEnable(true)
                    .isAccountNonExpired(true)
                    .isAccountNonLocked(true)
                    .isCredentialsNonExpired(true)
                    .roles(Set.of(roleAdmin))
                    .telefono("32909212")
                    .nombreCompleto("admin")
                    .build();

            Usuario usuarioCliente = Usuario.builder()
                    .email("cliente@gmail.com")
                    .password("1234")
                    .isEnable(true)
                    .isAccountNonExpired(true)
                    .isAccountNonLocked(true)
                    .isCredentialsNonExpired(true)
                    .roles(Set.of(roleUser))
                    .telefono("32909389212")
                    .nombreCompleto("cliente")
                    .build();

            usuarioRepository.saveAll(List.of(usuarioAdmin, usuarioCliente));

        };
    }
}
