package com.backend.sistemarestaurante.modules.pedidos;

import com.backend.sistemarestaurante.modules.mesas.Mesa;
import com.backend.sistemarestaurante.modules.pedidos.enums.EstadoPedidoEnum;
import com.backend.sistemarestaurante.modules.pedidos.enums.TipoServicio;
import com.backend.sistemarestaurante.modules.platos.Plato;
import com.backend.sistemarestaurante.modules.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // Tipo de servicio
    @Column(name = "tipo_servicio", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoServicio tipoServicio;

    // Detalles segun tipo de servicio
    private LocalDateTime horaRecogida; // Para RECOGER_PEDIDO

    // Campos OPCIONALES (solo para domicilio)
    private String direccionEntrega;

    // Campo para teléfono de contacto en caso de domicilio o recoger pedido
    private String telefonoContacto;
    private LocalDateTime fechaPedido;

    // Estado del pedido
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPedidoEnum estadoPedidoEnum = EstadoPedidoEnum.BORRADOR;

    // Realcion DetallePedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

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

    // Método para agregar detalles fácilmente
    public void agregarDetalle(Plato plato, Integer cantidad, String notas) {
        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(this);
        detalle.setPlato(plato);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(plato.getPrecio()); // Precio actual del plato
        detalle.setSubtotal(plato.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
        detalle.setNotas(notas);

        this.detalles.add(detalle);
    }

    // Método para calcular totales
    public void calcularTotales() {
        this.subtotal = detalles.stream()
                .map(DetallePedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.iva = this.subtotal.multiply(BigDecimal.valueOf(0.19)); // 19% IVA
        this.total = this.subtotal.add(this.iva);
    }

}
