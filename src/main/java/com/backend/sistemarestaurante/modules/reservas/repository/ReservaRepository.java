package com.backend.sistemarestaurante.modules.reservas.repository;

import com.backend.sistemarestaurante.modules.reservas.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByFechaReservaAndHoraReservaAndMesaIdAndEstado(
            LocalDate fechaReserva,
            LocalTime horaReserva,
            Long mesaId,
            String estado
    );

    List<Reserva> findByUsuarioEmail(String usuarioEmail);

    Optional<Reserva> findByIdAndUsuarioEmail(Long id, String usuarioEmail);
}
