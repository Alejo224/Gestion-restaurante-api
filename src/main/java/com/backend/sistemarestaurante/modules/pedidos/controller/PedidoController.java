package com.backend.sistemarestaurante.modules.pedidos.controller;

import com.backend.sistemarestaurante.modules.pedidos.dto.CancelarPedidoRequest;
import com.backend.sistemarestaurante.modules.pedidos.dto.PedidoRequest;
import com.backend.sistemarestaurante.modules.pedidos.dto.PedidoResponse;
import com.backend.sistemarestaurante.modules.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PedidoResponse> crearPedido(@RequestBody PedidoRequest request,
                                                      Authentication authentication){

        // Obtener el UserDetails desde Authentication
        String username = (String) authentication.getPrincipal();

        PedidoResponse response = pedidoService.crearPedido(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Obtener pedidos del usuario autenticado
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<PedidoResponse>> obtenerMisPedidos(
            Authentication authentication) {

        String email = (String) authentication.getPrincipal(); // El email del token

        List<PedidoResponse> pedidos = pedidoService.obtenerPedidosPorUsuario(email);
        return ResponseEntity.ok(pedidos);
    }

    // Listar todos los pedidos de usuarios
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidoResponse>> obtenerTodosLosPedidos(){
        List<PedidoResponse> pedidos = pedidoService.obtenerTodosLosPedidos();
        return ResponseEntity.ok(pedidos);
    }
    
    // Cancelar pedido
    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PedidoResponse> cancelarPedido(@PathVariable Long id, @RequestBody CancelarPedidoRequest request){
        PedidoResponse response = pedidoService.cancelarPedido(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
