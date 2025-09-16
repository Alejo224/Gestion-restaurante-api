package com.backend.sistemarestaurante.modules.categoriasPlatos;

import com.backend.sistemarestaurante.modules.categoriasPlatos.dto.CategoriaPlatoDto;
import com.backend.sistemarestaurante.modules.categoriasPlatos.dto.CategoriaResponseDto;
import com.backend.sistemarestaurante.shared.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaPlatoService {
    // Inyeccion de dependencias
    @Autowired
    private CategoriaPlatoRepository categoriaPlatoRepository;

    @Autowired
    private ModelMapper modelMapper; // Spring inyecta el bean de ModelMapper

    // Listar categorias de platos
    public List<CategoriaResponseDto> getAll(){

        // Obtener todas las entidades de la db
        List<CategoriaPlato> categorias = (List<CategoriaPlato>) categoriaPlatoRepository.findAll();

        // Convetir la lista de entidades a lista de DTOs
        return categorias.stream()
                .map( categoria -> modelMapper.map(categoria, CategoriaResponseDto.class))
                .collect(Collectors.toList());
    }

    // Listar categoria de plato por id
    public CategoriaResponseDto getById(Long id) {
        CategoriaPlato categoria = categoriaPlatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));

        return modelMapper.map(categoria, CategoriaResponseDto.class);
    }

    // Crear categoria de plato
    public CategoriaResponseDto create(CategoriaPlatoDto categoriaPlatoDto){
        // Convertir el DTO a entidad
        CategoriaPlato nuevaCategoria = modelMapper.map(categoriaPlatoDto, CategoriaPlato.class);
        // Guardar la nueva categoria en la db
        nuevaCategoria = categoriaPlatoRepository.save(nuevaCategoria);
        // Retornar el DTO de respuesta
        return modelMapper.map(nuevaCategoria, CategoriaResponseDto.class);
    }

    // Actualizar categoria de plato
    public CategoriaResponseDto update(Long id, CategoriaPlatoDto dto) {
        // Buscar la entidad por el id
        CategoriaPlato categoriaExistente = categoriaPlatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        // Copiar propiedades del DTO a la entidad (ignorando el ID)
        modelMapper.map(dto, categoriaExistente);

        // Guardar (usando la variable correcta: categoriaExistente)
        CategoriaPlato categoriaActualizada = categoriaPlatoRepository.save(categoriaExistente);

        // Convertir a DTO de respuesta y retornar
        return modelMapper.map(categoriaActualizada, CategoriaResponseDto.class);
    }

    // Eliminar categoria de plato
    public void delete(Long id){
        // Buscar la categoria de plato por id o lanzar excepcion si no existe
        if (!categoriaPlatoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada con id: " + id);
        }

        // Eliminación
        categoriaPlatoRepository.deleteById(id);
    }

}
