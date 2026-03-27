package com.taskflow.taskflow.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Data;

@Data

@Entity
@Table(name = "utilisateur")
public class Utilisateur {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;

    @Column(nullable = false)
    private String email;

    private String motDePasse;

    private LocalDateTime dateCreation;


}
