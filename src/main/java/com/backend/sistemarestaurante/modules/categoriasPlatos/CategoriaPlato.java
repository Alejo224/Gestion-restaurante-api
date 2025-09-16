package com.backend.sistemarestaurante.modules.categoriasPlatos;

import com.backend.sistemarestaurante.modules.platos.Plato;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true) // Solo incluye los campos con @ToString.Include
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categoria_plato")
public class CategoriaPlato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include // Incluir en el toString
    private Long id;
    
    @Column(nullable = false, length = 100)
    @ToString.Include // Incluir en el toString
    private String nombreCategoria;

    /** Relación UNO-a-MUCHOS con Plato
     mappedBy = "categoria" se refiere al atributo 'categoria' en la clase Plato
     CascadeType.ALL: si se elimina una categoría, se eliminan sus platos (¡CUIDADO! Quizás no quieras esto)
     FetchType.LAZY: por performance, no carga los platos hasta que se pidan explícitamente
     **/
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // ¡NO se incluye en @ToString y no necesita @EqualsAndHashCode.Exclude!
    private List<Plato> platos = new ArrayList<>();
}
