package com.backend.sistemarestaurante.modules.usuarios;

import com.backend.sistemarestaurante.modules.Roles.RoleEntity;
import com.backend.sistemarestaurante.modules.Roles.RoleEnum;
import com.backend.sistemarestaurante.modules.Roles.RoleRepository;
import com.backend.sistemarestaurante.modules.usuarios.dto.LoginRequestDto;
import com.backend.sistemarestaurante.modules.usuarios.dto.LoginResponseDto;
import com.backend.sistemarestaurante.modules.usuarios.dto.UsuarioRequestDto;
import com.backend.sistemarestaurante.modules.usuarios.dto.UsuarioResponseDto;
import com.backend.sistemarestaurante.shared.exceptions.DuplicateEmailException;
import com.backend.sistemarestaurante.shared.exceptions.DuplicateTelefonoException;
import com.backend.sistemarestaurante.shared.exceptions.InvalidPasswordException;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * La clase `UsuarioService` proporciona los servicios para la gestión de usuarios en la aplicación.
 * Actúa como la capa intermedia entre el controlador y el repositorio, facilitando la lógica de negocio
 * y la manipulación de datos.
 * Esta clase implementa las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad `Usuario` utilizando `UsuarioRepository`. Además, emplea `ModelMapper`
 * para transformar los objetos de transferencia de datos (DTOs) en entidades y viceversa.
 */
@Service
public class UsuarioService {
    /*
        *Inyectar el repositorio de usuarios y el model mapper
        * para convertir entre entidades y DTOs
        * Encriptar la contraseña del usuario
        * Inyectar el repository de RoleEntity
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    // metodo crear usuario
    public UsuarioResponseDto registrarUsuario(UsuarioRequestDto requestDto){
        // validar que el email no este registrado
        if (usuarioRepository.existsByEmail(requestDto.getEmail())){
            throw new DuplicateEmailException("El email ya está registrado");
        }

        // validar que el telefono no exista
        if (usuarioRepository.existsByTelefono(requestDto.getTelefono())){
            throw new DuplicateTelefonoException("El telefono ya está registrado");
        }

        // OBTENER ROL USER POR DEFECTO
        RoleEntity rolUSer = roleRepository.findByRoleEnum(RoleEnum.USER)
                .orElseThrow(() -> new IllegalArgumentException("El rol por defecto no existe"));

        // convertir dto a entidad utilizando Model Mapper
        Usuario usuario = modelMapper.map(requestDto, Usuario.class);

        // codificar contraseña
        usuario.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        // configuracion de seguridad por default
        configurarSeguridadPorDefecto(usuario);

        // Asignar rol por default
        usuario.setRoles(Set.of(rolUSer));

        // Guardar usuario en la base de datos
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return modelMapper.map(usuarioGuardado, UsuarioResponseDto.class);
    }

    // metodo crear admin
    public UsuarioResponseDto registrarAdmin(UsuarioRequestDto requestDto){
        // validar que el email no este registrado
        if (usuarioRepository.existsByEmail(requestDto.getEmail())){
            throw new DuplicateEmailException("El email ya está registrado");
        }

        // validar que el telefono no exista
        if (usuarioRepository.existsByTelefono(requestDto.getTelefono())){
            throw new DuplicateTelefonoException("El telefono ya está registrado");
        }

        // OBTENER ROL ADMIN
        RoleEntity rolAdmin = roleRepository.findByRoleEnum(RoleEnum.ADMIN)
                .orElseThrow(() -> new IllegalArgumentException("El rol por defecto no existe"));

        // convertir dto a entidad utilizando Model Mapper
        Usuario usuario = modelMapper.map(requestDto, Usuario.class);

        // codificar contraseña
        usuario.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        // configuracion de seguridad por default
        configurarSeguridadPorDefecto(usuario);

        // Asignar rol por default
        usuario.setRoles(Set.of(rolAdmin));

        // Guardar usuario en la base de datos
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return modelMapper.map(usuarioGuardado, UsuarioResponseDto.class);
    }

    // Metodo para logiarse
    public LoginResponseDto login(LoginRequestDto loginRequest){
        try {
            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Si llega aqui, la autenticacion fue exitosa
            Usuario usuario = usuarioRepository.findUsuarioByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            return new LoginResponseDto(
                    "Inicio de sesion exitoso",
                    usuario.getNombreCompleto(),
                    usuario.getEmail(),
                    usuario.getTelefono(),
                    usuario.getRoles()
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Credenciales incorrectas");
        }
    }

    // metodo obtener usuario por id
    public Usuario getById(Long id){
        //Buscar usuario por id
        return usuarioRepository.findById(id).orElse(null);
    }

    //metodo para obtener todos los usuarios
    public List<Usuario> getAll(){
        //Buscar todos los usuarios
        return usuarioRepository.findAll();
    }

    //metodo para eliminar usuario por id
    public void delete(Long id){
        //Eliminar usuario por id
        usuarioRepository.deleteById(id);
    }

    // metodo para la confifuracion de seguridad por defecto para usuario
    private void configurarSeguridadPorDefecto(Usuario usuario) {
        usuario.setEnable(true);
        usuario.setAccountNonExpired(true);
        usuario.setAccountNonLocked(true);
        usuario.setCredentialsNonExpired(true);
    }
}
