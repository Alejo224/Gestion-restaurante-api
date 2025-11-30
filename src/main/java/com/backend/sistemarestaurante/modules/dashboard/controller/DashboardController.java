package com.backend.sistemarestaurante.modules.dashboard.controller;

import com.backend.sistemarestaurante.modules.dashboard.dto.*;
import com.backend.sistemarestaurante.modules.dashboard.service.DashboardService;
import com.backend.sistemarestaurante.modules.dashboard.service.DashboardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final DashboardServiceImpl dashboardServiceImpl; // Para el método adicional

    @GetMapping("/resumen")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResumenDashboardDTO> resumenGeneral(){
        return ResponseEntity.ok(dashboardService.resumenGeneral());
    }

    @GetMapping("/ventas")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<FechaMontoDTO>> ventasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin){
        return ResponseEntity.ok(dashboardService.vestasPorFecha(inicio, fin));
    }

    @GetMapping("/top-platos")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<TopPlatoDTO>> topPlatos(@RequestParam(defaultValue = "10") Integer limit){
        return ResponseEntity.ok(dashboardService.topPlatos(limit));
    }

    @GetMapping("/pedidos-estados")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<PedidoEstadoDTO>> pedidosPorEstado(){
        return ResponseEntity.ok(dashboardService.pedidosPorEstado());
    }

    // NUEVO ENDPOINT PARA LAS ESTADÍSTICAS ESPECÍFICAS DEL DASHBOARD
    @GetMapping("/estadisticas-hoy")
    @PreAuthorize("permitAll()")
    public ResponseEntity<DashboardStatsDTO> getEstadisticasHoy() {
        return ResponseEntity.ok(dashboardServiceImpl.obtenerEstadisticasHoy());
    }
}