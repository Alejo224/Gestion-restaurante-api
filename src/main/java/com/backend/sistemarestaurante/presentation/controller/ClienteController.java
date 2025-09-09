package com.backend.sistemarestaurante.presentation.controller;

import com.backend.sistemarestaurante.persistence.entity.Cliente;
import com.backend.sistemarestaurante.service.implementation.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de clientes.
 * Proporciona endpoints para operaciones CRUD sobre la entidad {@link Cliente}.
 */
@Controller
@CrossOrigin
@RequestMapping("api/clientes")
@RestController
public class ClienteController {

    // Inyectar el servicio de clientes
    private ClienteService clienteService;

    // Obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> getAll(){
        return ResponseEntity.ok(clienteService.getAll());
    }
}
