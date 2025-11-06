package com.example.studentservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
}
