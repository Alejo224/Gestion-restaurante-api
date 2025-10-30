package com.backend.sistemarestaurante.modules.categoriasPlatos;

import com.backend.sistemarestaurante.modules.categoriasPlatos.dto.CategoriaPlatoDto;
import com.backend.sistemarestaurante.modules.categoriasPlatos.dto.CategoriaResponseDto;
import com.backend.sistemarestaurante.modules.platos.PlatoService;
import com.backend.sistemarestaurante.modules.platos.dto.PlatoResponseDto;
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

@RestController
@RequestMapping("api/categoriasPlatos")
@PreAuthorize("isAuthenticated()")
@Tag(
        name = "Category Management",
        description = "Operations for managing dish categories and their relationships"
)
public class CategoriaPlatoController {

    @Autowired
    private CategoriaPlatoService categoriaPlatoService;

    @Autowired
    private PlatoService platoService;

    /**
     * GET ALL CATEGORIES - Public
     */
    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Get all categories",
            description = "Retrieve a list of all dish categories. Public access.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved category list"
                    )
            }
    )
    public ResponseEntity<List<CategoriaResponseDto>> getAll() {
        List<CategoriaResponseDto> categoriasPlatos = categoriaPlatoService.getAll();
        return ResponseEntity.ok(categoriasPlatos);
    }

    /**
     * GET CATEGORY BY ID - Public
     */
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Get category by ID",
            description = "Retrieve a specific category by its ID. Public access.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Category ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved category"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found"
                    )
            }
    )
    public ResponseEntity<CategoriaResponseDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(categoriaPlatoService.getById(id));
    }

    /**
     * CREATE CATEGORY - Admin only
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create new category",
            description = "Create a new dish category. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Security Token"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Category created successfully"
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
    public ResponseEntity<CategoriaResponseDto> create(@RequestBody CategoriaPlatoDto categoriaPlatoDto){
        CategoriaResponseDto categoriaPlatoNueva = categoriaPlatoService.create(categoriaPlatoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaPlatoNueva);
    }

    /**
     * UPDATE CATEGORY - Admin only
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update category",
            description = "Update an existing category. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Security Token"),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Category ID to update",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category updated successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Insufficient permissions - ADMIN role required"
                    )
            }
    )
    public ResponseEntity<CategoriaResponseDto> update(@PathVariable Long id, @RequestBody CategoriaPlatoDto categoriaPlatoDto){
        CategoriaResponseDto response = categoriaPlatoService.update(id, categoriaPlatoDto);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE CATEGORY - Admin only
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete category",
            description = "Delete a category from the system. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Security Token"),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Category ID to delete",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Category deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Insufficient permissions - ADMIN role required"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Category cannot be deleted because it contains dishes"
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoriaPlatoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET DISHES BY CATEGORY - Public
     */
    @GetMapping("{id}/platos")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Get dishes by category",
            description = "Retrieve all dishes belonging to a specific category. Public access.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Category ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved dishes for category"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found"
                    )
            }
    )
    public ResponseEntity<List<PlatoResponseDto>> getPlatosPorCategoria(@PathVariable Long id){
        List<PlatoResponseDto> platos = platoService.getPlatosPorCategoria(id);
        return ResponseEntity.ok(platos);
    }
}