package com.backend.sistemarestaurante.modules.mesas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mesas")
@PreAuthorize("denyAll()") // Denegar todo el acceso por defecto
@Controller
public class MesaController {

    // Inyeccion de dependencias
    @Autowired
    private MesaService mesaService;

    // Metodo listar mesas
    @GetMapping
    @PreAuthorize("permitAll()") // Permitir acceso publico a este endpoint
    public ResponseEntity<List<Mesa>> getAll() {
        List<Mesa> listarMesas = mesaService.getAll();
        return ResponseEntity.ok(listarMesas);  // 200 OK
    }

    // Metodo buscar mesa por id
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ')") // Permitir acceso solo a usuarios con permiso de lectura
    public ResponseEntity<Mesa> getById(@PathVariable Long id) {
        Optional<Mesa> mesaOpt = mesaService.getById(id);
        return mesaOpt.map(ResponseEntity::ok) // 200 OK
                .orElseGet(ResponseEntity.notFound()::build); // 404 Not Found
    }

    // Metodo crear Mesa
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<Mesa> create(@RequestBody Mesa mesa) {
        Mesa nuevaMesa = mesaService.create(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa); // 201 Created
    }

    // Metodo actualizar Mesa
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<Mesa> update(@PathVariable Long id, @RequestBody Mesa mesa) {
        Optional<Mesa> mesaOpt = mesaService.getById(id);
        if (mesaOpt.isPresent()) {
            Mesa mesaToUpdate = mesaOpt.get();
            mesaToUpdate.setDescripcion(mesa.getDescripcion());
            mesaToUpdate.setEstado(mesa.isEstado());
            Mesa updatedMesa = mesaService.create(mesaToUpdate);
            return ResponseEntity.ok(updatedMesa); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Metodo eliminar Mesa
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<Mesa> delete(@PathVariable Long id){
        Optional<Mesa> mesaOpt = mesaService.getById(id);
        if (mesaOpt.isPresent()){
            mesaService.delete(id);
            return ResponseEntity.noContent().build(); //204 Not Content
        }else{
            return ResponseEntity.notFound().build(); //204 Not Found
        }
    }

}
