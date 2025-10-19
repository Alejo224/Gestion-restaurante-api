package com.backend.sistemarestaurante.modules.categoriasPlatos;

import com.backend.sistemarestaurante.modules.categoriasPlatos.dto.CategoriaPlatoDto;
import com.backend.sistemarestaurante.modules.categoriasPlatos.dto.CategoriaResponseDto;
import com.backend.sistemarestaurante.modules.platos.PlatoService;
import com.backend.sistemarestaurante.modules.platos.dto.PlatoResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/categoriasPlatos")
//@PreAuthorize("isAuthenticated()") // protección por defecto
public class CategoriaPlatoController {

    // Inteccion de dependencia
    @Autowired
    private CategoriaPlatoService categoriaPlatoService;
    
    @Autowired
    private PlatoService platoService;

    // Metodo listar platos
    @GetMapping
    @PreAuthorize("permitAll()") // Permitir acceso publico a este endpoint
    public ResponseEntity<List<CategoriaResponseDto>> getAll() {
        List<CategoriaResponseDto> categoriasPlatos = categoriaPlatoService.getAll();

        return ResponseEntity.ok(categoriasPlatos); // 200 OK
    }

    // Buscar categoria por id
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CategoriaResponseDto> getById(@PathVariable Long id){

        return ResponseEntity.ok(categoriaPlatoService.getById(id));

    }

    // Crear categoria plato
    @PostMapping
    @PreAuthorize("permitAll()") // Solo usuarios con rol ADMIN pueden crear categorias
    public ResponseEntity<CategoriaResponseDto> create(@RequestBody CategoriaPlatoDto categoriaPlatoDto){
        CategoriaResponseDto categoriaPlatoNueva = categoriaPlatoService.create(categoriaPlatoDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaPlatoNueva); // 201 Created
    }

    // Actualizar categoria plato
    @PutMapping("/{id}")
    @PreAuthorize("permitAll()") // Solo usuarios con rol ADMIN pueden crear categorias
    public ResponseEntity<CategoriaResponseDto> update(@PathVariable Long id, @RequestBody CategoriaPlatoDto categoriaPlatoDto){
        CategoriaResponseDto response = categoriaPlatoService.update(id, categoriaPlatoDto);

        return ResponseEntity.ok(response);
    }

    // Eliminar categoria plato por id
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo usuarios con rol ADMIN pueden crear categorias
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoriaPlatoService.delete(id);

        return ResponseEntity.noContent().build();
    }
    
    // Listar platos por categorias
    @GetMapping("{id}/platos")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<PlatoResponseDto>> getPlatosPorCategoria(@PathVariable Long id){
        List<PlatoResponseDto> platos = platoService.getPlatosPorCategoria(id);

        return ResponseEntity.ok(platos);   //200 OK
    }

}
