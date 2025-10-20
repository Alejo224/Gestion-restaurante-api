package com.backend.sistemarestaurante.configuration.security;

import com.backend.sistemarestaurante.modules.usuarios.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.LocalDateTime;
import java.util.List;

// Anotaciones
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    // inyectar UserDetailService
    @Autowired
    private UserDetailServiceImpl userDetailService;

    // Configuraciones de seguridad se implementaran aqui

    // Configurar el filter chain (configuraciones personalizadas)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults()) // Habilita el CORS configurado en el Bean
                .csrf(csrf -> csrf.disable())    // Desactiva CSRF para la API REST
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // Endpoints publicos
                    http.requestMatchers("/auth/**").permitAll();  // Login, registro
                    http.requestMatchers(HttpMethod.POST, "/api/usuarios/register").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/usuarios/register/admin").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/usuarios/login").permitAll();

                    // Acceso público a imágenes
                    http.requestMatchers("/images/**").permitAll();

                    http.requestMatchers(HttpMethod.GET ,"/api/platos").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/platos/{id}").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/categoriasPlatos").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/categoriasPlatos/{id}").permitAll();
                    http.requestMatchers(HttpMethod.GET, "api/categoriasPlatos/{id}/platos").permitAll();

                    //  ADMIN
                    http.requestMatchers(HttpMethod.POST, "/api/platos").permitAll();
                    http.requestMatchers(HttpMethod.PUT, "/api/platos/{id}").permitAll();
                    http.requestMatchers(HttpMethod.DELETE, "/api/platos/{id}").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/categoriasPlatos").permitAll();
                    http.requestMatchers(HttpMethod.PUT, "/api/categoriasPlatos/{id}").permitAll();
                    http.requestMatchers(HttpMethod.DELETE, "/api/categoriasPlatos/{id}").permitAll();

                    // Configurar el resto de endpoint - Requieren autenticación
                    http.anyRequest().authenticated();


                })
                // LOGOUT CONFIGURADO PARA FRONTEND
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")           // URL del logout
                        .logoutSuccessHandler(logoutSuccessHandler()) // Respuesta JSON
                        .invalidateHttpSession(true)             // Invalidar sesión
                        .deleteCookies("JSESSIONID")             // Limpiar cookies
                        .clearAuthentication(true)               // Limpiar seguridad
                )
                .authenticationProvider(authenticationProvider())  // CONECTAR provide
                .build();
    }

    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
    */

    // Configurar AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Proveedor de autenticacion
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provieder = new DaoAuthenticationProvider();
        provieder.setPasswordEncoder(passwordEncoder());
        provieder.setUserDetailsService(userDetailService);
        return provieder;
    }

    // Configuracion de la comparticion de recursos entre origines cruzados
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                "https://alejo224.github.io",  // Dominio de GitHub Pages
                "http://localhost:5173",        // Desarrollo local
                "http://127.0.0.1:5500"         // entorno actual (Live Server)
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Respuesta JSON para el frontend
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            String jsonResponse = """
                    {
                        "success": true,
                        "message": "Logout exitoso",
                        "timestamp": "%s"
                    }
                    """.formatted(LocalDateTime.now().toString());

            response.getWriter().write(jsonResponse);
        };
    }
    // Configuracion de los componenetes, PasswordEncoder y UserDetailsService
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
