package com.backend.sistemarestaurante.modules.reservas.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaRequestDTO {
    private LocalDate fechaReserva;
    private LocalTime horaReserva;
    private Integer numeroPersonas;
    private Long mesaId; // Solo recibimos el ID
    private String nota;
}
