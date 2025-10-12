package com.backend.sistemarestaurante.configuration.security;

import com.backend.sistemarestaurante.modules.usuarios.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

                    http.requestMatchers(HttpMethod.GET ,"/api/platos").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/platos/{id}").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/categoriasPlatos").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/categoriasPlatos/{id}").permitAll();

                    //  ADMIN
                    http.requestMatchers(HttpMethod.POST, "/api/platos").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/api/platos/{id}").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE, "/api/platos/{id}").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.POST, "/api/categoriasPlatos").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/api/categoriasPlatos/{id}").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE, "/api/categoriasPlatos/{id}").hasRole("ADMIN");

                    // Configurar el resto de endpoint - Requieren autenticación
                    http.anyRequest().authenticated();


                })
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
        configuration.setAllowedOriginPatterns(List.of("*")); // En producción cambia esto
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    // Configuracion de los componenetes, PasswordEncoder y UserDetailsService
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
