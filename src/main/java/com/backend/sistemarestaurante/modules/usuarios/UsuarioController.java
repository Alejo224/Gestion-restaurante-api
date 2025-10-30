package com.backend.sistemarestaurante.modules.usuarios;

import com.backend.sistemarestaurante.modules.usuarios.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for user management.
 * Provides endpoints for CRUD operations on the {@link Usuario} entity.
 */
@CrossOrigin
@RequestMapping("api/usuarios")
@RestController
@PreAuthorize("isAuthenticated()")
@Tag(
        name = "User Management",
        description = "Complete user management system including authentication, registration, and administrative operations"
)
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    /**
     * GET ALL USERS - Protected (Admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get All Users",
            description = "Retrieves a complete list of all registered users in the system. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Security Token"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved user list"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied - ADMIN role required"
                    )
            }
    )
    public ResponseEntity<List<Usuario>> getAll(){
        return ResponseEntity.ok(usuarioService.getAll());
    }

    /**
     * CLIENT REGISTRATION - Public (Automatic USER role)
     */
    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Register New Client",
            description = "Public endpoint for client self-registration. Automatically assigns USER role.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Client registration data",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterClientRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Client registered successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data or validation errors"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Email already exists in the system"
                    )
            }
    )
    public ResponseEntity<AuthResponse> registerClient(@RequestBody @Valid RegisterClientRequest request) {
        return new ResponseEntity<>(userDetailService.registerClient(request), HttpStatus.CREATED);
    }

    /**
     * USER LOGIN - Public
     */
    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "User Login",
            description = "Authenticates a user and returns JWT token with user details.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthLoginRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login successful"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid email or password"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Missing or invalid request format"
                    )
            }
    )
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.userDetailService.loginUser(userRequest), HttpStatus.OK);
    }

    /**
     * ADMIN REGISTRATION - Public (Initial setup)
     */
    @PostMapping("/register/admin")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Register Admin User",
            description = "Public endpoint for initial administrator registration. Assigns ADMIN role automatically.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Administrator registration data",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Administrator registered successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Admin user already exists"
                    )
            }
    )
    public ResponseEntity<UsuarioResponseDto> registrarAdmin(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto){
        UsuarioResponseDto responseDto = usuarioService.registrarAdmin(usuarioRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * CREATE USER (Admin only) - Protected
     */
    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create New User",
            description = "Administrative endpoint for creating new users with custom roles. Requires ADMIN role.",
            security = @SecurityRequirement(name = "Security Token"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User creation data with role assignment",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthCreateUserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid user data or role assignment"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Insufficient permissions - ADMIN role required"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User email already exists"
                    )
            }
    )
    public ResponseEntity<AuthResponse> createUser(@RequestBody @Valid AuthCreateUserRequest userRequest) {
        return new ResponseEntity<>(userDetailService.createUser(userRequest), HttpStatus.CREATED);
    }
}