package com.backend.sistemarestaurante.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Mesa")
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

}
