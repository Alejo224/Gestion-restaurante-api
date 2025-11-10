package com.backend.sistemarestaurante.modules.pedidos.repository;

import com.backend.sistemarestaurante.modules.pedidos.Pedido;
import org.springdoc.core.providers.JavadocProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar pedidos por ID de usuario
    List<Pedido> findByUsuarioId(Long usuarioId);

    // si prefieres buscar por email (más consistente con tu enfoque)
    List<Pedido> findByUsuarioEmail(String email);
}
