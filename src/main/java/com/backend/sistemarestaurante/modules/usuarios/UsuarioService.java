package com.backend.sistemarestaurante.modules.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * La clase `UsuarioService` proporciona los servicios para la gestión de usuarios en la aplicación.
 * Actúa como la capa intermedia entre el controlador y el repositorio, facilitando la lógica de negocio
 * y la manipulación de datos.
 * Esta clase implementa las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad `Usuario` utilizando `UsuarioRepository`. Además, emplea `ModelMapper`
 * para transformar los objetos de transferencia de datos (DTOs) en entidades y viceversa.
 */
@Service
public class UsuarioService {
    // Inyectar el repositorio de usuarios y el model mapper
    @Autowired
    private UsuarioRepository usuarioRepository;

    // metodo crear usuario
    public Usuario create(Usuario usuario){
        //Guardar usuario a la base de datos
        return usuarioRepository.save(usuario);
    }

    // metodo obtener usuario por id
    public Usuario getById(Long id){
        //Buscar usuario por id
        return usuarioRepository.findById(id).orElse(null);
    }

    //metodo para obtener todos los usuarios
    public List<Usuario> getAll(){
        //Buscar todos los usuarios
        return usuarioRepository.findAll();
    }

    //metodo para eliminar usuario por id
    public void delete(Long id){
        //Eliminar usuario por id
        usuarioRepository.deleteById(id);
    }
}
