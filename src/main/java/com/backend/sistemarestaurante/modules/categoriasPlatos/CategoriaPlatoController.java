package com.backend.sistemarestaurante.modules.categoriasPlatos;

import com.backend.sistemarestaurante.modules.categoriasPlatos.dto.CategoriaPlatoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/categoriasPlatos")
@PreAuthorize("denyAll()")  // Denegar to el acceso por defecto
@Controller
public class CategoriaPlatoController {

    // Inteccion de dependencia
    @Autowired
    private CategoriaPlatoService categoriaPlatoService;

    // Metodo listar platos
    @GetMapping
    @PreAuthorize("permitAll()") // Permitir acceso publico a este endpoint
    public ResponseEntity<List<CategoriaPlato>> getAll() {
        List<CategoriaPlato> categoriasPlatos = categoriaPlatoService.getAll();

        return ResponseEntity.ok(categoriasPlatos); // 200 OK
    }

    // Buscar categoria por id
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CategoriaPlato> getById(@PathVariable Long id){

        return categoriaPlatoService.getById(id)
                .map(ResponseEntity::ok) // 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 Not Found
    }

    // Crear categoria plato
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATE')")
    public ResponseEntity<CategoriaPlato> create(@RequestBody CategoriaPlato categoriaPlato){
        CategoriaPlato categoriaPlatoNueva = categoriaPlatoService.create(categoriaPlato);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaPlatoNueva); // 201 Created
    }

    // Actualizar categoria plato
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('CREATE')")
    public ResponseEntity<CategoriaPlato> update(@PathVariable Long id, @RequestBody CategoriaPlatoDto categoriaPlatoDto){
        CategoriaPlato categoriaPlatoUpdate = categoriaPlatoService.update(categoriaPlatoDto, id);

        return ResponseEntity.ok(categoriaPlatoUpdate);
    }

    // Eliminar categoria plato por id
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('CREATE')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoriaPlatoService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
