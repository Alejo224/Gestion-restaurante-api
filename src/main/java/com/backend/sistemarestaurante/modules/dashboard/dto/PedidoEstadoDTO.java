package com.backend.sistemarestaurante.modules.dashboard.dto;

import com.backend.sistemarestaurante.modules.pedidos.enums.EstadoPedidoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoEstadoDTO {
    private EstadoPedidoEnum estado;
    private Long cantidad;
}
