package com.backend.sistemarestaurante.modules.mesas;

import com.backend.sistemarestaurante.modules.mesas.dto.MesaRequestDTO;
import com.backend.sistemarestaurante.modules.mesas.dto.MesaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@PreAuthorize("isAuthenticated()")
@Controller
@Tag(
        name = "Table Management",
        description = "Operations for managing restaurant tables and their status"
)
public class MesaController {

    @Autowired
    private MesaService mesaService;

    /**
     * GET ALL TABLES - Admin and User access
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
            summary = "Get all tables",
            description = "Retrieve a list of all restaurant tables. Accessible by ADMIN and USER roles.",
            security = @SecurityRequirement(name = "Security Token"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved table list"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied - Authentication required"
                    )
            }
    )
    public ResponseEntity<List<MesaResponseDTO>> getAll() {
        List<MesaResponseDTO> listarMesas = mesaService.getAll();
        return ResponseEntity.ok(listarMesas);
    }

    /**
     * GET TABLE BY ID - Admin and User access
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
            summary = "Get table by ID",
            description = "Retrieve a specific table by its ID. Accessible by ADMIN and USER roles.",
            security = @SecurityRequirement(name = "Security Token"),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Table ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved table"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Table not found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied - Authentication required"
                    )
            }
    )
    public ResponseEntity<MesaResponseDTO> getById(@PathVariable Long id) {
        MesaResponseDTO mesa = mesaService.getById(id);
        return ResponseEntity.ok(mesa);
    }

    /**
     * CREATE TABLE - Admin only
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create new table",
            description = "Create a new restaurant table. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Security Token"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Table created successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Insufficient permissions - ADMIN role required"
                    )
            }
    )
    public ResponseEntity<MesaResponseDTO> create(@RequestBody MesaRequestDTO mesaRequestDTO) {
        MesaResponseDTO mesaNueva = mesaService.create(mesaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mesaNueva);
    }

    /**
     * UPDATE TABLE - Admin only
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update table",
            description = "Update an existing table's information. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Security Token"),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Table ID to update",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Table updated successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Table not found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Insufficient permissions - ADMIN role required"
                    )
            }
    )
    public ResponseEntity<MesaResponseDTO> update(@PathVariable Long id, @RequestBody MesaRequestDTO mesaRequestDTO) {
        MesaResponseDTO mesaActualizada = mesaService.update(id, mesaRequestDTO);
        return ResponseEntity.ok(mesaActualizada);
    }

    /**
     * DELETE TABLE - Admin only
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete table",
            description = "Delete a table from the system. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Security Token"),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Table ID to delete",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Table deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Table not found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Insufficient permissions - ADMIN role required"
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mesaService.eliminarMesa(id);
        return ResponseEntity.noContent().build();
    }
}