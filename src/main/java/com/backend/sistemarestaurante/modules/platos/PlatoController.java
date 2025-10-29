package com.backend.sistemarestaurante.modules.platos;

import com.backend.sistemarestaurante.modules.platos.dto.PlatoRequestDto;
import com.backend.sistemarestaurante.modules.platos.dto.PlatoResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/platos")
@PreAuthorize("isAuthenticated()") // protección por defecto
public class PlatoController {

    // inyeccion de dependencias
    @Autowired
    private PlatoService platoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    // CREAR PLATO CON IMAGEN
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlatoResponseDto> crearPlato(
            @RequestPart("plato") String platoRequestJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        try {
            // LIMPIAR el JSON - eliminar caracteres inválidos
            String jsonLimpio = platoRequestJson
                    .replace("?", "")  // Eliminar caracteres ?
                    .replace("\uFEFF", "") // Eliminar BOM si existe
                    .trim();

            System.out.println("JSON recibido: " + platoRequestJson);
            System.out.println("JSON limpio: " + jsonLimpio);

            // Convertir JSON limpio a objeto
            ObjectMapper objectMapper = new ObjectMapper();
            PlatoRequestDto platoRequest = objectMapper.readValue(jsonLimpio, PlatoRequestDto.class);

            PlatoResponseDto platoCreado = platoService.crearPlatoConImagen(platoRequest, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(platoCreado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // ACTUALIZAR PLATO COMPLETO (datos + imagen opcional)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlatoResponseDto> actualizarPlato(
            @PathVariable Long id,
            @RequestPart("plato") String platoRequestJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        try {
            // Convertir JSON a DTO
            ObjectMapper objectMapper = new ObjectMapper();
            PlatoRequestDto platoRequest = objectMapper.readValue(platoRequestJson, PlatoRequestDto.class);

            PlatoResponseDto platoActualizado = platoService.actualizarPlato(id, platoRequest, imagen);
            return ResponseEntity.ok(platoActualizado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    // Eliminar plato por id
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        platoService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
