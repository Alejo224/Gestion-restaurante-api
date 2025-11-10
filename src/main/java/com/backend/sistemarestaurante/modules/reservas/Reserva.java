package com.backend.sistemarestaurante.modules.reservas;

import com.backend.sistemarestaurante.modules.mesas.Mesa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;

    @Column(name = "hora_reserva", nullable = false)
    private LocalTime horaReserva;

    @Column(name = "numero_personas", nullable = false)
    private Integer numeroPersonas;

    // RELACIÓN CON MESA
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

    @Column(name = "usuario_email", nullable = false)
    private String usuarioEmail;

    @Column(name = "nota", length = 500)
    private String nota;

    @Column(name = "fecha_creacion")
    @Builder.Default    // Respeta el valor por defecto
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "estado")
    @Builder.Default    // Respeta el valor por defecto
    private String estado = "PENDIENTE"; // PENDIENTE, CONFIRMADA, CANCELADA

}
