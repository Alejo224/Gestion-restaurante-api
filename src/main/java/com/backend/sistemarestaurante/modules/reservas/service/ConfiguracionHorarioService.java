package com.backend.sistemarestaurante.modules.reservas.service;

import com.backend.sistemarestaurante.modules.reservas.ConfiguracionHorario;
import com.backend.sistemarestaurante.modules.reservas.repository.ConfiguracionHorarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfiguracionHorarioService {

    private final ConfiguracionHorarioRepository configuracionHorarioRepository;

    // Metodo para obtener disponibles
    public List<ConfiguracionHorario> obtenerHorariosDisponibles(){
        return configuracionHorarioRepository.findByActivoTrueOrderByHoraAsc();
    }

    // Metodo horarios por defecto
    public void inicializarHorariosPorDefecto(){
        String[] horariosPorDefecto = {
                "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
                "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"
        };

        for (String hora : horariosPorDefecto) {
            if (!configuracionHorarioRepository.existsByHora(hora)) {
                ConfiguracionHorario nuevoHorario = ConfiguracionHorario.builder()
                        .hora(hora)
                        .activo(true)
                        .build();
                configuracionHorarioRepository.save(nuevoHorario);
            }
        }
    }
}
