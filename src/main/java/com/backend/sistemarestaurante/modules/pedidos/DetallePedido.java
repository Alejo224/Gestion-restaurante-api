package com.backend.sistemarestaurante.modules.pedidos;

import com.backend.sistemarestaurante.modules.platos.Plato;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;


    // Relación con el plato
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plato_id", nullable = false)
    private Plato plato;

    // Cantidad del plato
    @Column(nullable = false)
    private Integer cantidad;

    // Precio en el momento del pedido (por si cambia después)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    // Subtotal para esta línea
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    // Notas específicas para este plato (ej: "sin cebolla")
    private String notas;
}