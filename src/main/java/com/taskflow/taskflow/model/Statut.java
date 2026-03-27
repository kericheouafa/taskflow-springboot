package com.taskflow.taskflow.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "statut")
public class Statut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String libelle;
}
