package com.backend.sistemarestaurante.configuration.app;

import com.backend.sistemarestaurante.modules.reservas.service.ConfiguracionHorarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ConfiguracionHorarioService configuracionHorarioService;

    @Override
    public void run(String... args) throws Exception {

        configuracionHorarioService.inicializarHorariosPorDefecto();
    }
}
