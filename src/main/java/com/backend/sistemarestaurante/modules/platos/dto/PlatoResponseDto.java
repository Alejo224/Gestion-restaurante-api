package com.backend.sistemarestaurante.modules.platos.dto;

import com.backend.sistemarestaurante.modules.categoriasPlatos.CategoriaPlato;
import com.backend.sistemarestaurante.modules.categoriasPlatos.dto.CategoriaPlatoDto;
import com.backend.sistemarestaurante.modules.categoriasPlatos.dto.CategoriaResponseDto;
import com.backend.sistemarestaurante.modules.platos.Plato;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.backend.sistemarestaurante.modules.platos.Plato}
 * Respuesta para el usuario
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatoResponseDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Boolean disponible;
    private CategoriaResponseDto categoria;
    private String imagenUrl;

    /*
    // Constructor desde la entidad
    public PlatoResponseDto(Plato plato) {
        this.id = plato.getId();
        this.nombre = plato.getNombre();
        this.descripcion = plato.getDescripcion();
        this.precio = plato.getPrecio();
        this.disponible = plato.getDisponible();
        this.imagenUrl = plato.getImagenUrl();
        this.categoria = new CategoriaResponseDto(plato.getCategoria());
    }

     */
}