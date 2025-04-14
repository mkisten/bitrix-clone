package com.kmtech.bitix_clone.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "engineers")
@Data
public class Engineer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String middleName;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;
}