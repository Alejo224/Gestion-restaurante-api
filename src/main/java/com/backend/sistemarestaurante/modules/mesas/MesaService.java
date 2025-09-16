package com.backend.sistemarestaurante.modules.mesas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MesaService {

    // Inyeccion de dependencias
    @Autowired
    private MesaRepository mesaRepository;

    //metodo listar mesas
    public List<Mesa> getAll() {
        return (List<Mesa>) mesaRepository.findAll();
    }

    // Metodo buscar mesa por id
    public Optional<Mesa> getById(Long id) {
        return mesaRepository.findById(id);
    }

    // Metodo crear Mesa
    public Mesa create(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    // Metodo actualizar Mesa
    public Mesa update(Mesa mesa){
        //Mesa mesaFormOb = getById(mesa.getId()).orElseThrow(ResourceNotFoundException::new);
        return null;
    }

    // Metodo eliminar Mesa
    public void delete(Long id) {
        mesaRepository.deleteById(id);
    }

}
