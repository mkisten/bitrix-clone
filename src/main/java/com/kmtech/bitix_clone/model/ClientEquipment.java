package com.kmtech.bitix_clone.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "client_equipment")
@Data
public class ClientEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Clients client; // Связь с клиентом

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand; // Марка оборудования

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Model model; // Модель оборудования

    @Column(nullable = false)
    private String serialNumber; // Серийный номер
}