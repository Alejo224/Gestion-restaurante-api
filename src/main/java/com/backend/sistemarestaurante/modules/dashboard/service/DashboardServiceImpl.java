package com.backend.sistemarestaurante.modules.dashboard.service;

import com.backend.sistemarestaurante.modules.dashboard.dto.*;
import com.backend.sistemarestaurante.modules.dashboard.repository.DashboardRepository;
import com.backend.sistemarestaurante.modules.pedidos.enums.EstadoPedidoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    @Override
    public ResumenDashboardDTO resumenGeneral() {
        // Obtener datos principales
        BigDecimal totalIngresos = dashboardRepository.totalIngresos(EstadoPedidoEnum.COMPLETADO);
        Long totalPedidos = dashboardRepository.totalPedidos();
        Long totalPedidosPagados = dashboardRepository.totalPedidosPorEstado(EstadoPedidoEnum.COMPLETADO);
        Long totalCancelados = dashboardRepository.totalPedidosPorEstado(EstadoPedidoEnum.CANCELADO);
        BigDecimal ticketPromedio = dashboardRepository.ticketPromedio(EstadoPedidoEnum.COMPLETADO);

        // Datos de hoy
        BigDecimal ventasHoy = dashboardRepository.ventasHoy(EstadoPedidoEnum.COMPLETADO);
        Long pedidosHoy = dashboardRepository.pedidosHoy();
        Long reservasHoy = dashboardRepository.reservasHoy();

        return new ResumenDashboardDTO(
                totalIngresos,
                totalPedidos,
                totalPedidosPagados,
                totalCancelados,
                ticketPromedio,
                ventasHoy,
                pedidosHoy,
                reservasHoy
        );
    }

    @Override
    public List<FechaMontoDTO> vestasPorFecha(LocalDate inicio, LocalDate fin) {
        return dashboardRepository.ventasPorFecha(inicio, fin, EstadoPedidoEnum.COMPLETADO);
    }

    @Override
    public List<TopPlatoDTO> topPlatos(Integer limit) {
        List<TopPlatoDTO> todosPlatos = dashboardRepository.topPlatos(EstadoPedidoEnum.COMPLETADO);

        // Manejar valores nulos y aplicar límite
        return todosPlatos.stream()
                .filter(Objects::nonNull)
                .map(this::sanitizarPlato)
                .limit(limit)
                .collect(Collectors.toList());
    }

    private TopPlatoDTO sanitizarPlato(TopPlatoDTO plato) {
        if (plato.getIngresosGenerados() == null) {
            plato.setIngresosGenerados(BigDecimal.ZERO);
        }
        if (plato.getTotalVendido() == null) {
            plato.setTotalVendido(0L);
        }
        if (plato.getNombrePlato() == null) {
            plato.setNombrePlato("Plato sin nombre");
        }
        return plato;
    }

    @Override
    public List<PedidoEstadoDTO> pedidosPorEstado() {
        return dashboardRepository.pedidosPorEstado();
    }

    // Método adicional para las estadísticas del dashboard que necesitas
    public DashboardStatusDTO obtenerEstadisticasHoy() {
        BigDecimal ingresosHoy = dashboardRepository.ventasHoy(EstadoPedidoEnum.COMPLETADO);
        Long pedidosHoy = dashboardRepository.pedidosHoy();
        Long reservasHoy = dashboardRepository.reservasHoy();
        Long mesasOcupadas = dashboardRepository.mesasOcupadas();
        Long totalMesas = dashboardRepository.totalMesas();
        Long platosDisponibles = dashboardRepository.platosDisponibles();
        Long reservasConfirmadasHoy = dashboardRepository.reservasConfirmadasHoy();

        // Calcular string de mesas ocupadas
        String mesasOcupadasStr = mesasOcupadas + "/" + totalMesas;

        // Crear DTO específico para el frontend
        return DashboardStatusDTO.builder()
                .reservasHoy(reservasHoy)
                .reservasConfirmadasHoy(reservasConfirmadasHoy)
                .mesasOcupadas(mesasOcupadasStr)
                .ingresosHoy(ingresosHoy)
                .platosActivos(platosDisponibles)
                .totalPedidosHoy(pedidosHoy)
                .build();
    }
}