package com.kmtech.bitix_clone.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "models")
@Data
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Название модели

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand; // Связь с маркой
}