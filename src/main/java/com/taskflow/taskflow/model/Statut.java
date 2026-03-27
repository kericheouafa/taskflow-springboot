package com.taskflow.taskflow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "statut")
public class Statut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String libelle;
}
