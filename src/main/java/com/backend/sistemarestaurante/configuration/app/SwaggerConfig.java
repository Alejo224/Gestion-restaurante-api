package com.backend.sistemarestaurante.configuration.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

/**
 * Configuración de Swagger/OpenAPI para la documentación automática de la API.
 *
 * <p>Esta clase define la configuración principal de OpenAPI 3.0 para el sistema de restaurante,
 * incluyendo metadatos, servidores y esquema de seguridad JWT.</p>
 *
 * <h3>URLs de acceso:</h3>
 * <ul>
 *   <li>Interfaz Swagger UI: <a href="http://localhost:8080/swagger-ui.html">http://localhost:8080/swagger-ui.html</a></li>
 *   <li>Documentación JSON: <a href="http://localhost:8080/v3/api-docs">http://localhost:8080/v3/api-docs</a></li>
 * </ul>
 *
 * <h3>Características configuradas:</h3>
 * <ul>
 *   <li>Información general del proyecto</li>
 *   <li>Múltiples entornos (Desarrollo/Producción)</li>
 *   <li>Autenticación JWT Bearer Token</li>
 *   <li>Documentación interactiva de endpoints</li>
 * </ul>
 *
 * @author Alejandro
 * @version 1.0.0
 */
@OpenAPIDefinition(
        // INFORMACIÓN GENERAL DE LA API
        info = @Info(
                title = "Sistema Restaurante API",
                description = """
                    Sistema de gestión integral para restaurantes.
                    
                    ## Funcionalidades Principales
                    - **Gestión de Menú**: CRUD completo de platos y categorías
                    - **Sistema de Pedidos**: Proceso completo de ordenes
                    - **Gestión de Mesas**: Control de disponibilidad y reservas
                    - **Autenticación JWT**: Seguridad con tokens bearer
                    - **Roles de Usuario**: ADMIN y USER con permisos diferenciados
                    
                    ## Tecnologías
                    - **Backend**: Spring Boot 3.x
                    - **Seguridad**: JWT + Spring Security
                    - **Base de Datos**: PostgreSQL
                    - **Documentación**: OpenAPI 3.0 (Swagger)
                    """,
                version = "1.0.0",
                contact = @Contact(
                        name = "Alejandro Angulo",
                        url = "https://www.linkedin.com/in/johan-alejandro-angulo-533516321",
                        email = "johantorresalejo224@gmail.com"
                )
        ),

        // CONFIGURACIÓN DE SERVIDORES (ENTORNOS)
        servers = {
                @Server(
                        description = " Servidor de Desarrollo Local",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = " Servidor de Producción",
                        url = "https://tu-dominio-produccion.com"
                )
        },

        // CONFIGURACIÓN DE SEGURIDAD GLOBAL
        security = @SecurityRequirement(
                name = "Security Token" // Nombre que debe coincidir con @SecurityScheme
        )
)
@SecurityScheme(

        // ESQUEMA DE AUTENTICACIÓN JWT
        name = "Security Token",
        description = """
        Autenticación mediante JWT (JSON Web Token).
        
        ## 🔐 Cómo usar:
        1. Obtén un token llamando a `/usuarios/login` (debe exister el usuario) o `/usuarios/register`
        2. Incluye el token en el header Authorization:
           ```
           Authorization: Bearer {tu-token-jwt}
           ```
        3. Los endpoints protegidos requerirán este token
         
        ## 👥 Roles disponibles:
        - **ADMIN**: Acceso completo a todos los endpoints
        - **USER**: Acceso limitado a funcionalidades básicas
        
        ## ⚠️ Nota:
        - El token expira después de un tiempo determinado (30 minutos)
        - Los endpoints marcados con 🔒 requieren autenticación
        """,
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    /**
     * Clase de configuración para Swagger/OpenAPI.
     *
     * <p>No requiere métodos adicionales ya que toda la configuración
     * se realiza mediante anotaciones a nivel de clase.</p>
     *
     * <h3>Configuración automática incluida:</h3>
     * <ul>
     *   <li>Generación de documentación interactiva</li>
     *   <li>Esquema de seguridad JWT</li>
     *   <li>Definición de múltiples servidores</li>
     *   <li>Metadatos del proyecto</li>
     * </ul>
     *
     * <h3>Uso en controladores:</h3>
     * <p>Para documentar endpoints individuales, usar anotaciones como:</p>
     * <pre>{@code
     * @Operation(summary = "Obtener todos los platos")
     * @Tag(name = "Gestión de Platos")
     * @SecurityRequirement(name = "Security Token")
     * }</pre>
     */

    // La clase está intencionalmente vacía porque toda la configuración
    // se maneja mediante las anotaciones a nivel de clase.
    // Spring Boot detectará automáticamente esta configuración al iniciar.
}