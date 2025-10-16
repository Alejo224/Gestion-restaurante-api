package com.backend.sistemarestaurante.modules.platos;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import java.io.IOException;

/*
*  servicio para manejar archivos
* */

@Service
@Slf4j
public class FileStorageService {

    @Value("${app.upload.dir:/app/uploads/images/platos/}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            File directorio = new File(uploadDir);
            if (!directorio.exists()) {
                boolean creado = directorio.mkdirs();
                log.info("🐳 VOLUME DOCKER - Directorio creado: {} en {}", creado, directorio.getAbsolutePath());

                // Verificar permisos del volumen
                log.info("🔒 Permisos volumen - Lectura: {}, Escritura: {}",
                        directorio.canRead(), directorio.canWrite());
            } else {
                log.info("✅ Directorio ya existe en volumen: {}", directorio.getAbsolutePath());
            }
        } catch (Exception e) {
            log.error("❌ Error inicializando volumen Docker: {}", e.getMessage());
        }
    }

    public String guardarImagen(MultipartFile archivo, Long platoId) {
        try {
            // Validar que la imagen no esté vacía
            if (archivo == null || archivo.isEmpty()) {
                throw new RuntimeException("La imagen es obligatoria");
            }

            // Verificar tipo de archivo
            String contentType = archivo.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("El archivo debe ser una imagen");
            }

            // Asegurar que el directorio existe
            File directorio = new File(uploadDir);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            // Generar nombre único para el archivo
            String extension = obtenerExtension(archivo.getOriginalFilename());
            String nombreArchivo = "plato_" + platoId + "_" + System.currentTimeMillis() + extension;
            String rutaCompleta = uploadDir + nombreArchivo;

            log.info("💾 Guardando en VOLUME Docker: {}", rutaCompleta);

            // Guardar archivo en el VOLUME
            archivo.transferTo(new File(rutaCompleta));

            log.info("✅ Imagen guardada exitosamente en volumen");

            // Retornar ruta relativa para acceso web
            return "/images/platos/" + nombreArchivo;

        } catch (IOException e) {
            log.error("❌ Error guardando en volumen Docker: {}", e.getMessage());
            throw new RuntimeException("Error al guardar la imagen en el volumen: " + e.getMessage(), e);
        }
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null) return ".jpg";
        int lastIndex = nombreArchivo.lastIndexOf(".");
        return lastIndex == -1 ? ".jpg" : nombreArchivo.substring(lastIndex);
    }
}