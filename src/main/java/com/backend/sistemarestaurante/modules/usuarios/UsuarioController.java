package com.backend.sistemarestaurante.modules.usuarios;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios.
 * Proporciona endpoints para operaciones CRUD sobre la entidad {@link Usuario}.
 */
@Controller
@CrossOrigin
@RequestMapping("api/usuarios")
@RestController
public class UsuarioController {

    // Inyectar el servicio de usuarios
    private UsuarioService usuarioService;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll(){
        return ResponseEntity.ok(usuarioService.getAll());
    }
}
