package com.backend.sistemarestaurante.modules.mesas;

import com.backend.sistemarestaurante.modules.reservas.Reserva;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entidad que representa la tabla en la base de datos.
 * <p>
 * La tabla asociada en la base de datos se llama "Mesa" y almacena la información
 * de cada mesa registrado en la aplicación.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Mesa")
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombreMesa;
    
    private boolean estado = true; // true = disponible, false = ocupada

    private Long capacidad;

    // Relacion con reserva
    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Reserva> reservas;
}
