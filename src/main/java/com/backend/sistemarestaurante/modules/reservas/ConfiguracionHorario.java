package com.backend.sistemarestaurante.modules.reservas;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "configuracion_horario")
public class ConfiguracionHorario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "hora", unique = true, nullable = false)
    private String hora;    // Formato "HH:mm" ej: "12:00", "14:30"

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private boolean activo = true;  // activo por defecto

}