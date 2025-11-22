package com.backend.sistemarestaurante.modules.pagos.controller;

import com.backend.sistemarestaurante.modules.pedidos.dto.PedidoRequest;
import com.backend.sistemarestaurante.modules.pagos.dto.SesionPagoResponse;
import com.backend.sistemarestaurante.modules.pagos.service.StripeService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/crear-sesion")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<SesionPagoResponse> crearSesionPago(
            @RequestBody PedidoRequest request,
            Authentication authentication) {

        String userEmail = (String) authentication.getPrincipal();
        SesionPagoResponse response = stripeService.crearSesionPago(request, userEmail);

        return ResponseEntity.ok(response);
    }
}