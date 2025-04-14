package com.kmtech.bitix_clone.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "clients")
@Data
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String surname;
    private String email;
    private String phone;
}
