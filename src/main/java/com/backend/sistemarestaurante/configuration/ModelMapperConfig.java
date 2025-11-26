package com.backend.sistemarestaurante.configuration;

import com.backend.sistemarestaurante.modules.pedidos.Pedido;
import com.backend.sistemarestaurante.modules.pedidos.dto.PedidoRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        // Configurar para que ignore estadoPedidoEnum al mapear desde Request
        modelMapper.typeMap(PedidoRequest.class, Pedido.class)
                .addMappings(mapper -> {
                    mapper.skip(Pedido::setEstadoPedidoEnum);
                    mapper.skip(Pedido::setId);
                    mapper.skip(Pedido::setUsuario);
                    mapper.skip(Pedido::setFechaPedido);
                });

        return modelMapper;
    }
}