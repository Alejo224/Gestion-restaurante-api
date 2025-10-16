package com.backend.sistemarestaurante.configuration.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

/*
* Configurar recursos estáticos
* */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:/app/uploads/images/platos/}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Exponer el VOLUME Docker como recurso estático
        registry.addResourceHandler("/images/platos/**")
                .addResourceLocations("file:" + uploadDir);

        log.info("🔗 Configurado acceso web a volumen: file:{}", uploadDir);
    }
}
