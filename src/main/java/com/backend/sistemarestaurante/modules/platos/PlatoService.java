package com.backend.sistemarestaurante.modules.platos;

import com.backend.sistemarestaurante.modules.categoriasPlatos.CategoriaPlato;
import com.backend.sistemarestaurante.modules.categoriasPlatos.CategoriaPlatoRepository;
import com.backend.sistemarestaurante.modules.categoriasPlatos.dto.CategoriaResponseDto;
import com.backend.sistemarestaurante.modules.platos.dto.PlatoRequestDto;
import com.backend.sistemarestaurante.modules.platos.dto.PlatoResponseDto;
import com.backend.sistemarestaurante.shared.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PlatoService {

    // Inyección de dependencias y métodos del servicio aquí
    @Autowired
    private PlatoRepository platoRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoriaPlatoRepository categoriaPlatoRepository;

    // Listar todos los platos
    public List<PlatoResponseDto> getAll(){
        // Obtener todas las entidades de la db
        List<Plato> platos = (List<Plato>) platoRepository.findAll();

        // Convertir la lista de entidades a lista de DTOs
        return platos.stream()
                .map(plato -> modelMapper.map(plato, PlatoResponseDto.class))
                .toList();
    }

    // Buscar plato por id
    public PlatoResponseDto getById(Long id){
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con id: " + id));

        // Convertir la entidad a DTO
        return modelMapper.map(plato, PlatoResponseDto.class);
    }

    // listar platos por categoria
    public List<PlatoResponseDto> getPlatosPorCategoria(Long id){
        // Guardar los datos por id
        List<Plato> platos = platoRepository.findByCategoriaId(id);
        // List<CategoriaResponseDto> platos = categoriaPlatoRepository.findById(id);

        // Convertir la lista de entidades a lista de DTOs
        return platos.stream()
                .map(plato -> modelMapper.map(plato, PlatoResponseDto.class))
                .toList();
    }

    // Metodo crear plato
    public PlatoResponseDto create(PlatoRequestDto platoRequestDto){
        // 1. Buscar la categoría por ID
        CategoriaPlato categoria = categoriaPlatoRepository.findById(platoRequestDto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con id: " + platoRequestDto.getCategoriaId()
                ));

        // 2. Convertir DTO a Entidad (sin la relación)
        Plato nuevoPlato = new Plato();
        nuevoPlato.setNombre(platoRequestDto.getNombre());
        nuevoPlato.setDescripcion(platoRequestDto.getDescripcion());
        nuevoPlato.setPrecio(platoRequestDto.getPrecio());
        nuevoPlato.setDisponible(platoRequestDto.getDisponible());
        nuevoPlato.setCategoria(categoria); // ← Establecer la relación manualmente

        // 3. Guardar
        Plato platoGuardado = platoRepository.save(nuevoPlato);

        // 4. Convertir a Response DTO
        PlatoResponseDto response = new PlatoResponseDto();
        response.setId(platoGuardado.getId());
        response.setNombre(platoGuardado.getNombre());
        response.setDescripcion(platoGuardado.getDescripcion());
        response.setPrecio(platoGuardado.getPrecio());
        response.setDisponible(platoGuardado.getDisponible());
        response.setCategoriaId(String.valueOf(platoGuardado.getCategoria().getId())); // ← Solo el ID

        return response;
    }

    // Metodo actualizar plato
    public PlatoResponseDto update(PlatoRequestDto platoRequestDto, Long id){
        // 1. Buscar el plato existente
        Plato platoExistente = platoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con id: " + id));

        // 2. Buscar la nueva categoría si es diferente
        if (!platoExistente.getCategoria().getId().equals(platoRequestDto.getCategoriaId())) {
            CategoriaPlato nuevaCategoria = categoriaPlatoRepository.findById(platoRequestDto.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoría no encontrada con id: " + platoRequestDto.getCategoriaId()
                    ));
            platoExistente.setCategoria(nuevaCategoria);
        }

        // 3. Actualizar otros campos
        platoExistente.setNombre(platoRequestDto.getNombre());
        platoExistente.setDescripcion(platoRequestDto.getDescripcion());
        platoExistente.setPrecio(platoRequestDto.getPrecio());
        platoExistente.setDisponible(platoRequestDto.getDisponible());

        // 4. Guardar
        Plato platoActualizado = platoRepository.save(platoExistente);

        // 5. Convertir a Response DTO
        PlatoResponseDto response = new PlatoResponseDto();
        response.setId(platoActualizado.getId());
        response.setNombre(platoActualizado.getNombre());
        response.setDescripcion(platoActualizado.getDescripcion());
        response.setPrecio(platoActualizado.getPrecio());
        response.setDisponible(platoActualizado.getDisponible());
        response.setCategoriaId(String.valueOf(platoActualizado.getCategoria().getId()));

        return response;
    }

    // Metodo eliminar plato
    public void delete(Long id){
        // Buscar el plato por id o lanzar excepcion si no existe
        if (!platoRepository.existsById(id)){
            throw new ResourceNotFoundException("Plato no encontrado con id: " + id);
        }
        else {
            // Eliminacion
            platoRepository.deleteById(id);
        }
    }
}
