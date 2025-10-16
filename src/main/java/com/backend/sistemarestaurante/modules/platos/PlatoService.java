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
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private FileStorageService fileStorageService;

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


    // MÉTODO CORREGIDO PARA CREAR PLATO CON IMAGEN
    public PlatoResponseDto crearPlatoConImagen(PlatoRequestDto platoRequest, MultipartFile imagen) {
        try {
            // 1. Convertir el DTO a entidad (solo datos básicos)
            Plato plato = modelMapper.map(platoRequest, Plato.class);
            plato.setId(null);

            // 2. BUSCAR Y ASIGNAR LA CATEGORÍA COMPLETA MANUALMENTE
            CategoriaPlato categoria = categoriaPlatoRepository.findById(platoRequest.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + platoRequest.getCategoriaId()));
            plato.setCategoria(categoria);  // Asignar objeto completo, no solo ID

            // 3. Guardar el plato en la base de datos primero (para generar el ID)
            Plato platoGuardado = platoRepository.save(plato);

            // 4. Si hay imagen, guardarla físicamente y actualizar la ruta
            if (imagen != null && !imagen.isEmpty()) {
                String rutaImagen = fileStorageService.guardarImagen(imagen, platoGuardado.getId());
                platoGuardado.setImagenUrl(rutaImagen);
                platoGuardado = platoRepository.save(platoGuardado);
            }

            // 5. Convertir a DTO de respuesta
            return modelMapper.map(platoGuardado, PlatoResponseDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el plato: " + e.getMessage(), e);
        }
    }

    // MÉTODO PARA ACTUALIZAR PLATO (con o sin imagen)
    public PlatoResponseDto actualizarPlato(Long id, PlatoRequestDto platoRequest, MultipartFile imagen) {
        try {
            // 1. Buscar plato existente
            Plato platoExistente = platoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Plato no encontrado con ID: " + id));

            // 2. Actualizar datos básicos
            platoExistente.setNombre(platoRequest.getNombre());
            platoExistente.setDescripcion(platoRequest.getDescripcion());
            platoExistente.setPrecio(platoRequest.getPrecio());
            platoExistente.setDisponible(platoRequest.getDisponible());

            // 3. Actualizar categoría si es diferente
            if (!platoExistente.getCategoria().getId().equals(platoRequest.getCategoriaId())) {
                CategoriaPlato nuevaCategoria = categoriaPlatoRepository.findById(platoRequest.getCategoriaId())
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                platoExistente.setCategoria(nuevaCategoria);
            }

            // 4. Manejar imagen (opcional)
            if (imagen != null && !imagen.isEmpty()) {
                String nuevaRutaImagen = fileStorageService.guardarImagen(imagen, id);
                platoExistente.setImagenUrl(nuevaRutaImagen);
            }

            // 5. Guardar cambios
            Plato platoActualizado = platoRepository.save(platoExistente);

            // 6. Convertir a DTO de respuesta
            return modelMapper.map(platoActualizado, PlatoResponseDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el plato: " + e.getMessage(), e);
        }
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
