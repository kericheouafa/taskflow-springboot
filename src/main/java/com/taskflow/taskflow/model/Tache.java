package com.taskflow.taskflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tache")
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String titre;

    private int priorite;

    private LocalDateTime dateCreation;

    private LocalDateTime dateLimite;

    @ManyToOne
    @JoinColumn(name = "id_statut")
    private Statut statut;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;
}