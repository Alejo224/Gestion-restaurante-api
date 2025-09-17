package com.backend.sistemarestaurante.modules.platos;

import com.backend.sistemarestaurante.modules.platos.dto.PlatoRequestDto;
import com.backend.sistemarestaurante.modules.platos.dto.PlatoResponseDto;
import com.backend.sistemarestaurante.shared.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatoService {

    // Inyección de dependencias y métodos del servicio aquí
    @Autowired
    private PlatoRepository platoRepository;
    @Autowired
    private ModelMapper modelMapper;

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
        List<Plato> platos = platoRepository.findByCategoriaI(id);

        // Convertir la lista de entidades a lista de DTOs
        return platos.stream()
                .map(plato -> modelMapper.map(plato, PlatoResponseDto.class))
                .toList();
    }

    // Metodo crear plato
    public PlatoResponseDto create(PlatoRequestDto platoRequestDto){
        // Convertir el DTO a entidad
        Plato nuevoPlato = modelMapper.map(platoRequestDto, Plato.class);

        // Guardar el nuevo plato al db
        nuevoPlato = platoRepository.save(nuevoPlato);

        // Retornar el DTO de respuesta
        return modelMapper.map(nuevoPlato, PlatoResponseDto.class);
    }

    // Metodo actualizar plato
    public PlatoResponseDto update(PlatoRequestDto platoResquestDto, Long id){
        // Buscar la entidad por el id
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con id: " + id));

        // Copiar entidad del DTO a la entidad (ignorando el ID)
        modelMapper.map(platoResquestDto, plato);

        // Guardar (usando la variable correcta: plato)
        Plato platoActualizado = platoRepository.save(plato);

        // Convertir a DTO de respuesta y retornar
        return modelMapper.map(platoActualizado, PlatoResponseDto.class);
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
