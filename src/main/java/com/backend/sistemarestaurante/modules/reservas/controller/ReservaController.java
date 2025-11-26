package com.backend.sistemarestaurante.modules.reservas.controller;

import com.backend.sistemarestaurante.modules.reservas.Reserva;
import com.backend.sistemarestaurante.modules.reservas.dto.ReservaRequestDTO;
import com.backend.sistemarestaurante.modules.reservas.dto.ReservaResponseDTO;
import com.backend.sistemarestaurante.modules.reservas.repository.ReservaRepository;
import com.backend.sistemarestaurante.modules.reservas.service.ReservaService;
import jakarta.servlet.http.PushBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
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
            @AuthenticationPrincipal String usuarioEmail) {

        ReservaResponseDTO reservaCreada = reservaService.crearReserva(reservaDTO, usuarioEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaCreada);
    }

    // Obtener reservas del usuario autenticado
    @GetMapping("/mis-reservas")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ReservaResponseDTO>> getMisReservas(
            @AuthenticationPrincipal String usuarioEmail) {

        List<ReservaResponseDTO> misReservas = reservaService.obtenerReservasPorUsuario(usuarioEmail);
        return ResponseEntity.ok(misReservas);
    }

    // Actualizar reserva (TU NUEVO ENDPOINT)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ReservaResponseDTO> actualizarReserva(
            @PathVariable Long id,
            @RequestBody ReservaRequestDTO reservaRequestDTO,
            @AuthenticationPrincipal String usuarioEmail) {

        ReservaResponseDTO reservaActualizada =
                reservaService.actualizarReserva(id, reservaRequestDTO, usuarioEmail);

        return ResponseEntity.ok(reservaActualizada);
    }

    // Cancelar reserva (solo si es del usuario)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> cancelarReserva(
            @PathVariable Long id,
            @AuthenticationPrincipal String usuarioEmail) {

        reservaService.cancelarReserva(id, usuarioEmail);
        return ResponseEntity.noContent().build();
    }

    // Obtener todas las reservas (ADMIN)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservaResponseDTO>> getAllReservas() {

        List<ReservaResponseDTO> todasReservas = reservaService.obtenerTodasLasReservas();
        return ResponseEntity.ok(todasReservas);
    }

    // Mesas ocupadas según fecha y hora
    @GetMapping("/mesas-ocupadas")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerMesasOcupadas(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("hora") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {

        List<ReservaResponseDTO> mesasOcupadasIds =
                reservaService.obtenerMesasOcupadas(fecha, hora);

        return ResponseEntity.ok(mesasOcupadasIds);
    }
    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> cancelarReservacion(
            @PathVariable Long id,
            @AuthenticationPrincipal String usuarioEmail) {

        reservaService.cancelarReserva(id, usuarioEmail);
        return ResponseEntity.noContent().build();
    }
}
