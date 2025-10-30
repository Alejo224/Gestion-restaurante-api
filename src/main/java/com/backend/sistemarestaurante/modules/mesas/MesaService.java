package com.backend.sistemarestaurante.modules.mesas;

import com.backend.sistemarestaurante.modules.mesas.dto.MesaRequestDTO;
import com.backend.sistemarestaurante.modules.mesas.dto.MesaResponseDTO;
import com.backend.sistemarestaurante.shared.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MesaService {

    // Inyeccion de dependencias
    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ModelMapper modelMapper;

    //metodo listar mesas
    public List<MesaResponseDTO> getAll() {
        // Obtener todos las mesas registradas en db
        List<Mesa> mesas = (List<Mesa>) mesaRepository.findAll();

        // Convetir la lista de entidades a lista de DTOs
        return mesas.stream()
                .map(mesa -> modelMapper.map(mesa, MesaResponseDTO.class))
                .collect(Collectors.toList());
    }

    // Metodo buscar mesa por id
    public MesaResponseDTO getById(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con el id: " + id));

        // Convertir a DTO
        return modelMapper.map(mesa, MesaResponseDTO.class);

    }

    // Metodo crear Mesa
    public MesaResponseDTO create(MesaRequestDTO mesaRequestDTO) {
        // Convertir el dto a entidad
        Mesa nuevaMesa = modelMapper.map(mesaRequestDTO, Mesa.class);

        // Guardar el objecto convertido a la base de datos
        nuevaMesa = mesaRepository.save(nuevaMesa);

        // Devolver el DTO de respuesta
        return modelMapper.map(nuevaMesa, MesaResponseDTO.class);
    }

    // Metodo actualizar Mesa
    public MesaResponseDTO update(Long id, MesaRequestDTO mesaRequestDTO){
        
        // Verificar si la mesa exista
        Mesa mesaExistente = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con el id: " + id));
        
        // Copiar propiedades del DTO a la entidad (ignorando el ID)
        modelMapper.map(mesaRequestDTO, mesaExistente);

        // Guardar (usando la variable correcta: categoriaExistente)
        Mesa mesaActualizada = mesaRepository.save(mesaExistente);

        // Convertir a DTO de respuesta y retornar
        return modelMapper.map(mesaActualizada, MesaResponseDTO.class);
    }

    // Metodo eliminar Mesa
    public void eliminarMesa(Long id) {
        
        // Verifiacr si existe la mesa que desea eliminar
        if (!mesaRepository.existsById(id)){
            throw new ResourceNotFoundException("Mesa no encontrada con id: " + id);
        }
        
        // Eliminacion
        mesaRepository.deleteById(id);
    }

}
