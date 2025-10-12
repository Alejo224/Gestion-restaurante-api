package com.backend.sistemarestaurante.modules.platos;

import com.backend.sistemarestaurante.modules.platos.dto.PlatoRequestDto;
import com.backend.sistemarestaurante.modules.platos.dto.PlatoResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platos")
@PreAuthorize("isAuthenticated()") // protección por defecto
public class PlatoController {

    // inyeccion de dependencias
    @Autowired
    private PlatoService platoService;

    // Controlador listar platos
    @GetMapping
    @PreAuthorize("permitAll()") // Permitir acceso publico a este endpoint
    public ResponseEntity<List<PlatoResponseDto>> getAll(){
        // Obtener la lista de platos desde la db
        List<PlatoResponseDto> platos = platoService.getAll();

        // Retornar la lista de platos
        return ResponseEntity.ok(platos);
    }

    // Buscar plato por id
    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<PlatoResponseDto> getById(@PathVariable Long id){

        // Retornar el plato encontrado por id
        return ResponseEntity.ok(platoService.getById(id));
    }

    // Crear plato
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo usuarios con rol ADMIN pueden crear platos
    public ResponseEntity<PlatoResponseDto> create(@RequestBody PlatoRequestDto platoRequestDto){
        // Crear el nuevo plato
        PlatoResponseDto nuevoPlato = platoService.create(platoRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPlato); // 2001 CREATED
    }

    // actualizar plato por id
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')") // solo usuarios con el rol ADMIN pueden actualizar platos
    public ResponseEntity<PlatoResponseDto> update(@PathVariable Long id, @RequestBody PlatoRequestDto platoRequestDto){
        PlatoResponseDto response = platoService.update(platoRequestDto, id);

        return ResponseEntity.ok().body(response);
    }

    // Eliminar plato por id
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        platoService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
