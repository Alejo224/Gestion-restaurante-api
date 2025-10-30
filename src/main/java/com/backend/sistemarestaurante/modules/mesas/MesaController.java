package com.backend.sistemarestaurante.modules.mesas;

import com.backend.sistemarestaurante.modules.mesas.dto.MesaRequestDTO;
import com.backend.sistemarestaurante.modules.mesas.dto.MesaResponseDTO;
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
@PreAuthorize("isAuthenticated()") // protección por defecto
@Controller
public class MesaController {

    // Inyeccion de dependencias
    @Autowired
    private MesaService mesaService;

    // Metodo listar mesas
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Permitir acceso publico a este endpoint
    public ResponseEntity<List<MesaResponseDTO>> getAll() {

        List<MesaResponseDTO> listarMesas = mesaService.getAll();

        return ResponseEntity.ok(listarMesas);  // 200 OK
    }

    // Metodo buscar mesa por id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Permitir acceso solo a usuarios con permiso de lectura
    public ResponseEntity<MesaResponseDTO> getById(@PathVariable Long id) {

        MesaResponseDTO mesa = mesaService.getById(id);

        return ResponseEntity.ok(mesa); //200 OK
    }

    // Metodo crear Mesa
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")   // Solamente los que tenga el rol de ADMIN pueden crear
    public ResponseEntity<MesaResponseDTO> create(@RequestBody MesaRequestDTO mesaRequestDTO) {
        MesaResponseDTO mesaNueva = mesaService.create(mesaRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(mesaNueva);   //201 CREATED
    }

    // Metodo actualizar Mesa
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")   // Solamente los que tenga el rol de ADMIN pueden actualizar
    public ResponseEntity<MesaResponseDTO> update(@PathVariable Long id, @RequestBody MesaRequestDTO mesaRequestDTO) {

        MesaResponseDTO mesaActualizada = mesaService.update(id, mesaRequestDTO);

        return ResponseEntity.ok(mesaActualizada);  //200 OK
    }

    // Metodo eliminar Mesa
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")   // Solamente los que tenga el rol de ADMIN pueden eliminar
    public ResponseEntity<Void> delete(@PathVariable Long id){

        mesaService.eliminarMesa(id);
        
        return ResponseEntity.noContent().build(); 
    }

}
