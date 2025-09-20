package com.backend.sistemarestaurante.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    /**
     * Bean de ModelMapper.
     * <p>
     * Proporciona una instancia de `ModelMapper`, utilizada para mapear automáticamente
     * objetos de entidades a DTOs y viceversa.
     *
     * @return Una instancia de `ModelMapper`.
     */
    @Bean
    public ModelMapper modelMapper(){
        // Instancia del modelMapper
        return new ModelMapper();
    }

}
