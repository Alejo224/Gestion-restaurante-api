package com.backend.sistemarestaurante.modules.reservas.controller;

import com.backend.sistemarestaurante.modules.reservas.ConfiguracionHorario;
import com.backend.sistemarestaurante.modules.reservas.service.ConfiguracionHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@PreAuthorize("isAuthenticated()")
public class ConfiguracionHorarioController {

    // Inyectar el servicio si es necesario
    @Autowired
    private ConfiguracionHorarioService configuracionHorarioService;

    @GetMapping("/disponibles")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ConfiguracionHorario>> obtenerHorariosDisponibles() {
        List<ConfiguracionHorario> horariosDisponibles = configuracionHorarioService.obtenerHorariosDisponibles();
        return ResponseEntity.ok(horariosDisponibles);
    }
}
