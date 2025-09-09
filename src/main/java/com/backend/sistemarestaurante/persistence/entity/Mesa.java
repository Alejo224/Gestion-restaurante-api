package com.backend.sistemarestaurante.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa la tabla en la base de datos.
 * <p>
 * La tabla asociada en la base de datos se llama "Mesa" y almacena la información
 * de cada mesa registrado en la aplicación.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Mesa")
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String descripcion;
    
    private boolean estado; // true = disponible, false = ocupada
}
