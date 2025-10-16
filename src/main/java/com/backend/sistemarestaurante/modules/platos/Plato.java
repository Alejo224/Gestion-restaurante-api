package com.backend.sistemarestaurante.modules.platos;


import com.backend.sistemarestaurante.modules.categoriasPlatos.CategoriaPlato;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


/**
 * Entidad que representa la tabla en la base de datos.
 * <p>
 * La tabla asociada en la base de datos se llama "Plato" y almacena la información
 * de cada plato registrado en la aplicación.
 */
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true) // Solo incluye los campos con @ToString.Include
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Plato")
public class Plato {
    //Identificar unico de cada plato
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include // Incluir en el toString
    private Long id;

    @Column(nullable = false, length = 100)
    @ToString.Include // Incluir en el toString
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Boolean disponible = true;

    private String imagenUrl;

    /** Relación MUCHOS a UNO con CategoriaPlato
     @JoinColumn define el nombre de la columna FK en la tabla 'plato'
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    // ¡Excluido automáticamente de toString por onlyExplicitlyIncluded = true!
    private CategoriaPlato categoria;
}
