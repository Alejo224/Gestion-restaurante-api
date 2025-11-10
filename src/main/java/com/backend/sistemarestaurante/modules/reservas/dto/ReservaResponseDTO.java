package com.backend.sistemarestaurante.modules.reservas.dto;

import com.backend.sistemarestaurante.modules.mesas.dto.MesaResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservaResponseDTO {
    private Long id;
    private LocalDate fechaReserva;
    private LocalTime horaReserva;
    private Integer numeroPersonas;
    private MesaResponseDTO mesa; // Enviamos el objeto completo de la mesa
    private String nota;
    private String estado;
    private LocalDateTime fechaCreacion;
    private String usuarioEmail;
}
