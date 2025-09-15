package com.backend.sistemarestaurante.service.implementation;

import com.backend.sistemarestaurante.persistence.entity.CategoriaPlato;
import com.backend.sistemarestaurante.persistence.repository.CategoriaPlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaPlatoService {
    // Inyeccion de dependencias
    @Autowired
    private CategoriaPlatoRepository categoriaPlatoRepository;

    // Listar categorias de platos
    public List<CategoriaPlato> getAll(){
        return (List<CategoriaPlato>) categoriaPlatoRepository.findAll();
    }

    // Listar categoria de plato por id
    public Optional<CategoriaPlato> getById(Long id){
        return categoriaPlatoRepository.findById(id);
    }

    // Crear categoria de plato
    public CategoriaPlato create(CategoriaPlato categoriaPlato){
        return categoriaPlatoRepository.save(categoriaPlato);
    }

    // Actualizar categoria de plato


}
