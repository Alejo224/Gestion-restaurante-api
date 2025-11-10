package com.backend.sistemarestaurante.modules.reservas.controller;

import com.backend.sistemarestaurante.modules.reservas.dto.ReservaRequestDTO;
import com.backend.sistemarestaurante.modules.reservas.dto.ReservaResponseDTO;
import com.backend.sistemarestaurante.modules.reservas.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reserva")
@PreAuthorize("isAuthenticated()")
public class ReservaController {

    private final ReservaService reservaService;

    // Crear reserva
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ReservaResponseDTO> crearReserva(
            @RequestBody ReservaRequestDTO reservaDTO,
            @AuthenticationPrincipal String usuarioEmail) {  // ← Email del token

        ReservaResponseDTO reservaCreada = reservaService.crearReserva(reservaDTO, usuarioEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservaCreada);
    }

    // Obtener reservas del usuario autenticado
    @GetMapping("/mis-reservas")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ReservaResponseDTO>> getMisReservas(@AuthenticationPrincipal String usuarioEmail) {
        List<ReservaResponseDTO> misReservas = reservaService.obtenerReservasPorUsuario(usuarioEmail);

        return ResponseEntity.ok(misReservas);
    }

    // Cancelar reserva (solo si es del usuario)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id, @AuthenticationPrincipal String usuarioEmail) {
        reservaService.cancelarReserva(id, usuarioEmail);
        return ResponseEntity.noContent().build();
    }
}
