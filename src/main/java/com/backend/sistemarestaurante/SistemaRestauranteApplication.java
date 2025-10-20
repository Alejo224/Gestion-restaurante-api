package com.backend.sistemarestaurante;

import com.backend.sistemarestaurante.modules.Roles.RoleEntity;
import com.backend.sistemarestaurante.modules.Roles.RoleEnum;
import com.backend.sistemarestaurante.modules.Roles.RoleRepository;
import com.backend.sistemarestaurante.modules.permissions.PermissionEntity;
import com.backend.sistemarestaurante.modules.usuarios.Usuario;
import com.backend.sistemarestaurante.modules.usuarios.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SistemaRestauranteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaRestauranteApplication.class, args);
    }


}