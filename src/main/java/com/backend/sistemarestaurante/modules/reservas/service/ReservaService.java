package com.backend.sistemarestaurante.modules.reservas.service;

import com.backend.sistemarestaurante.modules.mesas.Mesa;
import com.backend.sistemarestaurante.modules.mesas.MesaRepository;
import com.backend.sistemarestaurante.modules.mesas.dto.MesaResponseDTO;
import com.backend.sistemarestaurante.modules.reservas.ConfiguracionHorario;
import com.backend.sistemarestaurante.modules.reservas.Reserva;
import com.backend.sistemarestaurante.modules.reservas.dto.ReservaRequestDTO;
import com.backend.sistemarestaurante.modules.reservas.dto.ReservaResponseDTO;
import com.backend.sistemarestaurante.modules.reservas.repository.ReservaRepository;
import com.backend.sistemarestaurante.shared.exceptions.ResourceNotFoundException;
import com.backend.sistemarestaurante.shared.exceptions.business.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ConfiguracionHorarioService horarioService;
    private final MesaRepository mesaRepository;



    public ReservaResponseDTO crearReserva(ReservaRequestDTO reservaDTO, String usuarioEmail) {
        // Validar que el horario esté en los horarios permitidos
        List<ConfiguracionHorario> horariosPermitidos = horarioService.obtenerHorariosDisponibles();

        // Convertir LocalTime a String "HH:mm" para comparar
        String horaFormateada = reservaDTO.getHoraReserva().format(DateTimeFormatter.ofPattern("HH:mm"));

        boolean horarioValido = horariosPermitidos.stream()
                .anyMatch(horario -> horario.getHora().equals(horaFormateada));

        if (!horarioValido) {
            List<String> horariosDisponibles = horariosPermitidos.stream()
                    .map(ConfiguracionHorario::getHora)
                    .collect(Collectors.toList());

            throw new HorarioNoDisponibleException(
                    "Horario '" + horaFormateada + "' no disponible. Horarios válidos: " + String.join(", ", horariosDisponibles)
            );
        }

        // Buscar la mesa
        Mesa mesa = mesaRepository.findById(reservaDTO.getMesaId())
                .orElseThrow(() ->  new BusinessException("Mesa no encontrada con ID: " + reservaDTO.getMesaId(), "MESA_NO_ENCONTRADA"));

        // Validar que la mesa esté disponible
        if (!mesa.isEstado()) {
            throw new MesaNoDisponibleException("La mesa '" + mesa.getNombreMesa() + "' no está disponible para reservas");
        }

        // Validar capacidad
        if (reservaDTO.getNumeroPersonas() > mesa.getCapacidad()) {
            throw new CapacidadExcedidaException(
                    "Capacidad excedida. La mesa '" + mesa.getNombreMesa() + "' soporta máximo " +
                            mesa.getCapacidad() + " personas, pero se solicitaron " + reservaDTO.getNumeroPersonas()
            );
        }

        // Validar conflicto de reserva - CORRECTO
        boolean existeConflicto = reservaRepository.existsByFechaReservaAndHoraReservaAndMesaIdAndEstado(
                reservaDTO.getFechaReserva(),
                reservaDTO.getHoraReserva(),
                reservaDTO.getMesaId(),
                "CONFIRMADA"  // Solo considerar reservas confirmadas
        );

        if (existeConflicto) {
            throw new ConflictoReservaException(
                    "La mesa '" + mesa.getNombreMesa() + "' ya tiene una reserva confirmada para " +
                            reservaDTO.getFechaReserva() + " a las " + horaFormateada);
        }

        // Validar número de personas
        if (reservaDTO.getNumeroPersonas() < 1 || reservaDTO.getNumeroPersonas() > 7) {
            throw new BusinessException("El número de personas debe estar entre 1 y 7", "NUMERO_PERSONAS_INVALIDO");
        }

        // Crear la reserva
        Reserva reserva = Reserva.builder()
                .fechaReserva(reservaDTO.getFechaReserva())
                .horaReserva(reservaDTO.getHoraReserva())
                .numeroPersonas(reservaDTO.getNumeroPersonas())
                .mesa(mesa)
                .usuarioEmail(usuarioEmail)
                .nota(reservaDTO.getNota())
                .estado("CONFIRMADA")
                .build();

        Reserva reservaGuardada = reservaRepository.save(reserva);
        return convertirAResponseDTO(reservaGuardada);
    }

    // AGREGAR ESTE MÉTODO QUE TE FALTA
    private ReservaResponseDTO convertirAResponseDTO(Reserva reserva) {
        return ReservaResponseDTO.builder()
                .id(reserva.getId())
                .fechaReserva(reserva.getFechaReserva())
                .horaReserva(reserva.getHoraReserva())
                .numeroPersonas(reserva.getNumeroPersonas())
                .mesa(MesaResponseDTO.builder()
                        .id(reserva.getMesa().getId())
                        .nombreMesa(reserva.getMesa().getNombreMesa())
                        .estado(reserva.getMesa().isEstado())
                        .capacidad(Math.toIntExact(reserva.getMesa().getCapacidad()))
                        .build())
                .nota(reserva.getNota())
                .estado(reserva.getEstado())
                .fechaCreacion(reserva.getFechaCreacion())
                .usuarioEmail(reserva.getUsuarioEmail())
                .build();
    }

    // AGREGAR ESTOS MÉTODOS ADICIONALES QUE SEGURAMENTE NECESITARÁS
    public List<ReservaResponseDTO> obtenerReservasPorUsuario(String usuarioEmail) {
        return reservaRepository.findByUsuarioEmail(usuarioEmail)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    public ReservaResponseDTO obtenerReservaPorId(Long id, String usuarioEmail) {
        Reserva reserva = reservaRepository.findByIdAndUsuarioEmail(id, usuarioEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
        return convertirAResponseDTO(reserva);
    }

    public boolean cancelarReserva(Long id, String usuarioEmail) {
        Reserva reserva = reservaRepository.findByIdAndUsuarioEmail(id, usuarioEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
        return true;
    }

}
