package com.backend.sistemarestaurante.modules.usuarios;

import com.backend.sistemarestaurante.modules.Roles.RoleEntity;
import com.backend.sistemarestaurante.modules.Roles.RoleEnum;
import com.backend.sistemarestaurante.modules.Roles.RoleRepository;
import com.backend.sistemarestaurante.modules.usuarios.dto.AuthCreateUserRequest;
import com.backend.sistemarestaurante.modules.usuarios.dto.AuthLoginRequest;
import com.backend.sistemarestaurante.modules.usuarios.dto.AuthResponse;
import com.backend.sistemarestaurante.modules.usuarios.dto.RegisterClientRequest;
import com.backend.sistemarestaurante.shared.exceptions.DuplicateEmailException;
import com.backend.sistemarestaurante.shared.exceptions.DuplicateTelefonoException;
import com.backend.sistemarestaurante.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Carga un usuario por su email (username)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findUsuarioByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // Agregar roles
        usuario.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        // Agregar permisos
        usuario.getRoles().stream()
                .flatMap(role -> role.getPermissionSet().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getNombre())));

        return new User(usuario.getEmail(), usuario.getPassword(), usuario.isEnable(),
                usuario.isAccountNonExpired(), usuario.isCredentialsNonExpired(),
                usuario.isAccountNonLocked(), authorityList);
    }

    /**
     * Login de usuario
     */
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String email = authLoginRequest.email();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(email, "Inicio de sesión exitoso", accessToken, true);
    }

    /**
     * ✅ NUEVO: Registro de CLIENTE (público) - ROL USER por defecto
     */
    public AuthResponse registerClient(RegisterClientRequest request) {
        // Validar que las contraseñas coincidan
        if (!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("El email ya está registrado");
        }

        // Verificar si el telefono ya existe
        if (usuarioRepository.existsByTelefono(request.telefono())) {
            throw new DuplicateTelefonoException("El telefono ya está registrado");
        }

        // Obtener el rol USER de la base de datos
        RoleEntity userRole = roleRepository.findByRoleEnum(RoleEnum.USER)
                .orElseThrow(() -> new IllegalStateException("Rol USER no encontrado en la base de datos"));

        // Crear conjunto con solo el rol USER
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);

        // Construir el usuario cliente
        Usuario usuario = Usuario.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nombreCompleto(request.nombreCompleto())
                .telefono(request.telefono())
                .roles(roles)
                .isEnable(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .build();

        Usuario usuarioCreated = usuarioRepository.save(usuario);

        // Crear las autoridades
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        usuarioCreated.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        usuarioCreated.getRoles().stream()
                .flatMap(role -> role.getPermissionSet().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getNombre())));

        // Generar token
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                usuarioCreated.getEmail(),
                usuarioCreated.getPassword(),
                authorityList
        );

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(usuarioCreated.getEmail(), "Cliente registrado exitosamente", accessToken, true);
    }

    /**
     * ✅ Crear usuario (ADMIN) - Con roles personalizados
     * Este método solo debe ser usado por administradores
     */
    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) {
        String email = authCreateUserRequest.email();
        String password = authCreateUserRequest.password();

        // Validar que las contraseñas coincidan
        if (!password.equals(authCreateUserRequest.confirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        // Verificar si el email ya existe
        if (usuarioRepository.findUsuarioByEmail(email).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Obtener roles desde el request
        List<String> roleRequest = authCreateUserRequest.roleRequest().roleListName();

        Set<RoleEntity> roleEntitySet = new HashSet<>(roleRepository.findRoleEntitiesByRoleEnumIn(roleRequest));

        if (roleEntitySet.isEmpty()) {
            throw new IllegalArgumentException("Los roles especificados no existen");
        }

        // Construir el usuario
        Usuario usuario = Usuario.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nombreCompleto(authCreateUserRequest.nombreCompleto())
                .telefono(authCreateUserRequest.telefono())
                .roles(roleEntitySet)
                .isEnable(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .build();

        Usuario usuarioCreated = usuarioRepository.save(usuario);

        // Crear autoridades
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        usuarioCreated.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        usuarioCreated.getRoles().stream()
                .flatMap(role -> role.getPermissionSet().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getNombre())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                usuarioCreated.getEmail(),
                usuarioCreated.getPassword(),
                authorityList
        );

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(usuarioCreated.getEmail(), "Usuario creado exitosamente", accessToken, true);
    }

    /**
     * Autentica un usuario con email y contraseña
     */
    public Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.loadUserByUsername(email);

        if (userDetails == null) {
            throw new BadCredentialsException("Correo o contraseña inválidos");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Correo o contraseña inválidos");
        }

        return new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
    }
}