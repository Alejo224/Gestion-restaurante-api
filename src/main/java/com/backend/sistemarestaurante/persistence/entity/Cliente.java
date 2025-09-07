package com.backend.sistemarestaurante.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "email")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String correo;

    @Column(name = "telefono", unique = true, nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String telefono;

    @Column(name="byrth_date")
    private LocalDateTime fechaNacimiento;
}