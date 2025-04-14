package com.kmtech.bitix_clone.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "equipment")
@Data
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand; // Марка оборудования

    @Column(nullable = false)
    private String model; // Модель оборудования
}