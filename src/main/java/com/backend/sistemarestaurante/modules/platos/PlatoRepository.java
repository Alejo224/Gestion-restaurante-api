package com.backend.sistemarestaurante.modules.platos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlatoRepository extends JpaRepository<Plato, Long> {

    // Query para la categoria
    List<Plato> findByCategoriaId(Long categoriaId);

}
