package com.backend.sistemarestaurante.modules.platos;

import com.backend.sistemarestaurante.modules.platos.dto.PlatoRequestDto;
import com.backend.sistemarestaurante.modules.platos.dto.PlatoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@PreAuthorize("isAuthenticated()")
@Tag(
        name = "Dish Management",
        description = "Operations for managing restaurant dishes and menu items"
)
public class PlatoController {

    @Autowired
    private PlatoService platoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * GET ALL DISHES - Public
     */
    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Get all dishes",
            description = "Retrieve a list of all available dishes. Public access.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved dish list"
                    )
            }
    )
    public ResponseEntity<List<PlatoResponseDto>> getAll(){
        List<PlatoResponseDto> platos = platoService.getAll();
        return ResponseEntity.ok(platos);
    }

    /**
     * GET DISH BY ID - Public
     */
    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Get dish by ID",
            description = "Retrieve a specific dish by its ID. Public access.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Dish ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved dish"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Dish not found"
                    )
            }
    )
    public ResponseEntity<PlatoResponseDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(platoService.getById(id));
    }

    /**
     * CREATE DISH WITH IMAGE - Admin only
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create new dish",
            description = "Create a new dish with optional image upload. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Security Token"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Dish created successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data or JSON format"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Insufficient permissions - ADMIN role required"
                    )
            }
    )
    public ResponseEntity<PlatoResponseDto> crearPlato(
            @Parameter(
                    description = "Dish data in JSON format",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PlatoRequestDto.class))
            )
            @RequestPart("plato") String platoRequestJson,

            @Parameter(
                    description = "Dish image file (optional)",
                    required = false
            )
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        try {
            String jsonLimpio = platoRequestJson
                    .replace("?", "")
                    .replace("\uFEFF", "")
                    .trim();

            System.out.println("JSON recibido: " + platoRequestJson);
            System.out.println("JSON limpio: " + jsonLimpio);

            ObjectMapper objectMapper = new ObjectMapper();
            PlatoRequestDto platoRequest = objectMapper.readValue(jsonLimpio, PlatoRequestDto.class);

            PlatoResponseDto platoCreado = platoService.crearPlatoConImagen(platoRequest, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(platoCreado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * UPDATE DISH - Admin only
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update dish",
            description = "Update an existing dish with optional new image. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Security Token"),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Dish ID to update",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dish updated successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data or JSON format"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Dish not found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Insufficient permissions - ADMIN role required"
                    )
            }
    )
    public ResponseEntity<PlatoResponseDto> actualizarPlato(
            @PathVariable Long id,

            @Parameter(
                    description = "Updated dish data in JSON format",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PlatoRequestDto.class))
            )
            @RequestPart("plato") String platoRequestJson,

            @Parameter(
                    description = "New dish image file (optional)",
                    required = false
            )
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PlatoRequestDto platoRequest = objectMapper.readValue(platoRequestJson, PlatoRequestDto.class);

            PlatoResponseDto platoActualizado = platoService.actualizarPlato(id, platoRequest, imagen);
            return ResponseEntity.ok(platoActualizado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE DISH - Admin only
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete dish",
            description = "Delete a dish by its ID. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Security Token"),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Dish ID to delete",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Dish deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Dish not found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Insufficient permissions - ADMIN role required"
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id){
        platoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}