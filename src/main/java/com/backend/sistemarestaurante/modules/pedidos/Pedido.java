package com.backend.sistemarestaurante.modules.pedidos;

import com.backend.sistemarestaurante.modules.mesas.Mesa;
import com.backend.sistemarestaurante.modules.pedidos.enums.TipoServicio;
import com.backend.sistemarestaurante.modules.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con usuario (cliente) que realiza el pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Fecha pedido
    private LocalDateTime fechaPedido;

    // Tipo de servicio
    @Column(name = "tipo_servicio", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoServicio tipoServicio;

    // Detalles segun tipo de servicio
    private LocalDateTime horaRecogida; // Para RECOGER_PEDIDO

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mesa_id")
    private Mesa mesa; // Para COMER_EN_RESTAURANTE

    // Para domicilio
    private String direccionEntrega;

    // Totales
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal iva;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    // Estado
    @Column(columnDefinition = "TEXT")
    private String notas;
    


}
