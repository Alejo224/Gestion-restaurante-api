package com.backend.sistemarestaurante.modules.dashboard.service;

import com.backend.sistemarestaurante.modules.dashboard.dto.*;
import java.time.LocalDate;
import java.util.List;

public interface DashboardService {

    ResumenDashboardDTO resumenGeneral();

    List<FechaMontoDTO> vestasPorFecha(LocalDate inicio, LocalDate fin);

    List<TopPlatoDTO> topPlatos(Integer limit);

    List<PedidoEstadoDTO> pedidosPorEstado();
}