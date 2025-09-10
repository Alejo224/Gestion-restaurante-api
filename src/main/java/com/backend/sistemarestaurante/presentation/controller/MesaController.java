package com.backend.sistemarestaurante.presentation.controller;

import com.backend.sistemarestaurante.persistence.entity.Mesa;
import com.backend.sistemarestaurante.service.implementation.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mesas")
@Controller
public class MesaController {

    // Inyeccion de dependencias
    @Autowired
    private MesaService mesaService;

    // Metodo listar mesas
    @GetMapping
    public ResponseEntity<List<Mesa>> getAll() {
        List<Mesa> listarMesas = mesaService.getAll();
        return ResponseEntity.ok(listarMesas);  // 200 OK
    }

    // Metodo buscar mesa por id
    @GetMapping("/{id}")
    public ResponseEntity<Mesa> getById(@PathVariable Long id) {
        Optional<Mesa> mesaOpt = mesaService.getById(id);
        return mesaOpt.map(ResponseEntity::ok) // 200 OK
                .orElseGet(ResponseEntity.notFound()::build); // 404 Not Found
    }

    // Metodo crear Mesa
    @PostMapping
    public ResponseEntity<Mesa> create(@RequestBody Mesa mesa) {
        Mesa nuevaMesa = mesaService.create(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa); // 201 Created
    }

    // Metodo actualizar Mesa
    @PutMapping("/{id}")
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
