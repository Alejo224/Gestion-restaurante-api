package com.backend.sistemarestaurante.modules.categoriasPlatos;

import com.backend.sistemarestaurante.modules.categoriasPlatos.dto.CategoriaPlatoDto;
import com.backend.sistemarestaurante.shared.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaPlatoService {
    // Inyeccion de dependencias
    @Autowired
    private CategoriaPlatoRepository categoriaPlatoRepository;

    @Autowired
    private ModelMapper modelMapper; // Spring inyecta el bean de ModelMapper

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
    public CategoriaPlato update(CategoriaPlatoDto categoriaPlatoDto, Long id){
        // Buscar la categoria de plato por id o lanzar excepcion si no existe
        CategoriaPlato categoriaPlatoFormb = getById(id).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        // Copiar propiedades del DTO a la entidad existente
        modelMapper.map(categoriaPlatoDto, categoriaPlatoFormb);

        // Guardar y retornar la entidad actualizada
        return categoriaPlatoRepository.save(categoriaPlatoFormb);
    }

    // Eliminar categoria de plato
    public void delete(Long id){
        // Buscar la categoria de plato por id o lanzar excepcion si no existe
        CategoriaPlato categoriaPlatoFormb = getById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));

        // Eliminar la categoria de plato
        categoriaPlatoRepository.deleteById(id);
    }

}
