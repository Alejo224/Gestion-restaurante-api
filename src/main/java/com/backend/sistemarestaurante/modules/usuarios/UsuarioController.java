package com.backend.sistemarestaurante.modules.usuarios;

import com.backend.sistemarestaurante.modules.usuarios.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de usuarios.
 * Proporciona endpoints para operaciones CRUD sobre la entidad {@link Usuario}.
 */

@CrossOrigin
@RequestMapping("api/usuarios")
@RestController
@PreAuthorize("isAuthenticated()") // protección por defecto
public class UsuarioController {

    // Inyectar el servicio de usuarios
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll(){
        return ResponseEntity.ok(usuarioService.getAll());
    }

    // Registrar un nuevo usuario
    /*
    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UsuarioResponseDto> registrarUsuario(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto){
        UsuarioResponseDto responseDto = usuarioService.registrarUsuario(usuarioRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

     */

    /**
     * REGISTRO DE CLIENTE - Público (ROL USER automático)
     */
    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthResponse> registerClient(@RequestBody @Valid RegisterClientRequest request) {
        return new ResponseEntity<>(userDetailService.registerClient(request), HttpStatus.CREATED);
    }


    // login
    /*@PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            LoginResponseDto response = usuarioService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }

    }

     */
    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
    return new ResponseEntity<>(this.userDetailService.loginUser(userRequest), HttpStatus.OK);
    }

    // Registrar un nuevo usuario
    @PostMapping("/register/admin")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UsuarioResponseDto> registrarAdmin(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto){
        UsuarioResponseDto responseDto = usuarioService.registrarAdmin(usuarioRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * CREAR USUARIO (ADMIN) - Protegido (Solo ADMIN puede crear usuarios con roles personalizados)
     */
    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> createUser(@RequestBody @Valid AuthCreateUserRequest userRequest) {
        return new ResponseEntity<>(userDetailService.createUser(userRequest), HttpStatus.CREATED);
    }
}
