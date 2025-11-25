package com.backend.sistemarestaurante.modules.pagos.controller;

import com.backend.sistemarestaurante.modules.pagos.dto.PagoPedidoExistenteRequest;
import com.backend.sistemarestaurante.modules.pedidos.Pedido;
import com.backend.sistemarestaurante.modules.pedidos.dto.PedidoRequest;
import com.backend.sistemarestaurante.modules.pagos.dto.SesionPagoResponse;
import com.backend.sistemarestaurante.modules.pagos.service.StripeService;
import com.backend.sistemarestaurante.modules.pedidos.dto.PedidoResponse;
import com.backend.sistemarestaurante.modules.pedidos.enums.EstadoPedidoEnum;
import com.backend.sistemarestaurante.modules.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PagoController {

    private final StripeService stripeService;
    private final PedidoService pedidoService;

    @PostMapping("/crear-sesion")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<SesionPagoResponse> crearSesionPago(
            @RequestBody PedidoRequest request,
            Authentication authentication) {

        String userEmail = (String) authentication.getPrincipal();
        SesionPagoResponse response = stripeService.crearSesionPago(request, userEmail);

        return ResponseEntity.ok(response);
    }

    // Endpoint para pagar pedido existente
    @PostMapping("/crear-sesion-pedido-existente")
    public ResponseEntity<SesionPagoResponse> crearSesionParaPedidoExistente(
            @RequestBody PagoPedidoExistenteRequest request,
            Authentication authentication) {

        try {
            String userEmail = (String) authentication.getPrincipal();

            System.out.println("Usuario autenticado: " + userEmail);
            System.out.println("Solicitando pago para pedido: " + request.getPedidoId());
            System.out.println("Email del cliente: " + request.getCustomerEmail());

            // Buscar el pedido existente usando tu PedidoService
            PedidoResponse pedidoResponse = pedidoService.obtenerPedidoPorId(request.getPedidoId());

            // Convertir a entidad Pedido (necesitarás este método en tu PedidoService)
            Pedido pedidoExistente = pedidoService.obtenerPedidoEntityPorId(request.getPedidoId());

            // Verificar que el pedido pertenece al usuario
            if (!pedidoExistente.getUsuario().getEmail().equals(userEmail)) {
                System.err.println("El pedido no pertenece al usuario");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Crear sesión de Stripe para el pedido existente
            SesionPagoResponse response = stripeService.crearSesionPagoParaPedidoExistente(
                    pedidoExistente,
                    request.getCustomerEmail()
            );

            System.out.println("Sesión creada exitosamente: " + response.getSessionId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error en crearSesionParaPedidoExistente: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}