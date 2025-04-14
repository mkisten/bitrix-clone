package com.kmtech.bitix_clone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Clients client;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "engineer_id")
    private Engineer engineer;

    @NotNull(message = "Type must not be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType type;

    private String description;
    private BigDecimal serviceCost;
    private BigDecimal transportCost;
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}