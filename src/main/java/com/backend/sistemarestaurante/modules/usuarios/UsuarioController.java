package com.backend.sistemarestaurante.modules.usuarios;

import com.backend.sistemarestaurante.modules.usuarios.dto.UsuarioRequestDto;
import com.backend.sistemarestaurante.modules.usuarios.dto.UsuarioResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll(){
        return ResponseEntity.ok(usuarioService.getAll());
    }

    // Registrar un nuevo usuario
    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UsuarioResponseDto> registrarUsuario(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto){
        UsuarioResponseDto responseDto = usuarioService.registrarUsuario(usuarioRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
