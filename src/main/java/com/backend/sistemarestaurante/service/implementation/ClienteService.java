package com.backend.sistemarestaurante.service.implementation;

import com.backend.sistemarestaurante.persistence.entity.Cliente;
import com.backend.sistemarestaurante.persistence.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * La clase `ClienteService` proporciona los servicios para la gestión de clientes en la aplicación.
 * Actúa como la capa intermedia entre el controlador y el repositorio, facilitando la lógica de negocio
 * y la manipulación de datos.
 * Esta clase implementa las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad `Cliente` utilizando `ClienteRepository`. Además, emplea `ModelMapper`
 * para transformar los objetos de transferencia de datos (DTOs) en entidades y viceversa.
 */
@Service
public class ClienteService {
    // Inyectar el repositorio de clientes y el model mapper
    @Autowired
    private ClienteRepository clienteRepository;

    // metodo crear cleinte
    public Cliente create(Cliente cliente){
        //Guardar Cliente a la base de datos
        return clienteRepository.save(cliente);
    }

    // metodo obtener cliente por id
    public Cliente getById(Long id){
        //Buscar cliente por id
        return clienteRepository.findById(id).orElse(null);
    }

    //metodo para obtener todos los clientes
    public List<Cliente> getAll(){
        //Buscar todos los clientes
        return clienteRepository.findAll();
    }

    //metodo para eliminar cliente por id
    public void delete(Long id){
        //Eliminar cliente por id
        clienteRepository.deleteById(id);
    }
}
