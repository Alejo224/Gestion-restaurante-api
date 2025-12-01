package com.backend.sistemarestaurante.modules.dashboard.repository;

import com.backend.sistemarestaurante.modules.dashboard.dto.*;
import com.backend.sistemarestaurante.modules.pedidos.Pedido;
import com.backend.sistemarestaurante.modules.pedidos.enums.EstadoPedidoEnum;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DashboardRepository extends JpaRepository<Pedido, Long> {

    // ===========================
    //      RESUMEN GENERAL
    // ===========================

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.estadoPedidoEnum = :estado")
    BigDecimal totalIngresos(@Param("estado") EstadoPedidoEnum estado);

    @Query("SELECT COUNT(p) FROM Pedido p")
    Long totalPedidos();

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estadoPedidoEnum = :estado")
    Long totalPedidosPorEstado(@Param("estado") EstadoPedidoEnum estado);

    // Ticket promedio de pedidos COMPLETADOS
    @Query("SELECT COALESCE(AVG(p.total), 0) FROM Pedido p WHERE p.estadoPedidoEnum = :estado")
    BigDecimal ticketPromedio(@Param("estado") EstadoPedidoEnum estado);

    // ===========================
    //        VENTAS POR FECHA
    // ===========================

    @Query("""
           SELECT new com.backend.sistemarestaurante.modules.dashboard.dto.FechaMontoDTO(
               CAST(p.fechaPedido AS localDate),
               COALESCE(SUM(p.total), 0)
           )
           FROM Pedido p
           WHERE p.estadoPedidoEnum = :estado
             AND CAST(p.fechaPedido AS localDate) BETWEEN :inicio AND :fin
           GROUP BY CAST(p.fechaPedido AS localDate)
           ORDER BY CAST(p.fechaPedido AS localDate)
           """)
    List<FechaMontoDTO> ventasPorFecha(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin,
            @Param("estado") EstadoPedidoEnum estado
    );

    // ===========================
    //        TOP PLATOS
    // ===========================

    @Query("""
           SELECT new com.backend.sistemarestaurante.modules.dashboard.dto.TopPlatoDTO(
                   dp.plato.nombre,
                   SUM(dp.cantidad),
                   SUM(dp.subtotal)
               )
           FROM DetallePedido dp
           JOIN dp.pedido p
           WHERE p.estadoPedidoEnum = :estado
           GROUP BY dp.plato.id, dp.plato.nombre
           ORDER BY SUM(dp.cantidad) DESC
           """)
    List<TopPlatoDTO> topPlatos(@Param("estado") EstadoPedidoEnum estado);

    // ===========================
    //    PEDIDOS POR ESTADO
    // ===========================

    @Query("""
           SELECT new com.backend.sistemarestaurante.modules.dashboard.dto.PedidoEstadoDTO(
               p.estadoPedidoEnum,
               COUNT(p)
           )
           FROM Pedido p
           GROUP BY p.estadoPedidoEnum
           """)
    List<PedidoEstadoDTO> pedidosPorEstado();

    // ===========================
    //     DATOS DEL DÍA HOY
    // ===========================

    @Query("""
           SELECT COALESCE(SUM(p.total), 0)
           FROM Pedido p
           WHERE CAST(p.fechaPedido AS localDate) = CURRENT_DATE
             AND p.estadoPedidoEnum = :estado
           """)
    BigDecimal ventasHoy(@Param("estado") EstadoPedidoEnum estado);

    @Query("""
           SELECT COUNT(p)
           FROM Pedido p
           WHERE CAST(p.fechaPedido AS localDate) = CURRENT_DATE
           """)
    Long pedidosHoy();

    @Query("""
           SELECT COUNT(p)
           FROM Pedido p
           WHERE CAST(p.fechaPedido AS localDate) = CURRENT_DATE 
             AND p.estadoPedidoEnum = :estado
           """)
    Long pedidosHoyPorEstado(@Param("estado") EstadoPedidoEnum estado);

    @Query("""
           SELECT COUNT(r)
           FROM Reserva r
           WHERE r.fechaReserva = CURRENT_DATE
           """)
    Long reservasHoy();

    @Query("""
           SELECT COUNT(r)
           FROM Reserva r
           WHERE r.fechaReserva = CURRENT_DATE
             AND r.estado = 'CONFIRMADA'
           """)
    Long reservasConfirmadasHoy();

    // ===========================
    //     MESAS Y PLATOS
    // ===========================

    @Query("SELECT COUNT(m) FROM Mesa m WHERE m.estado = false")
    Long mesasOcupadas();

    @Query("SELECT COUNT(m) FROM Mesa m")
    Long totalMesas();

    @Query("SELECT COUNT(p) FROM Plato p WHERE p.disponible = true")
    Long platosDisponibles();
}