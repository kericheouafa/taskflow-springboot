package com.taskflow.taskflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tache")
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)

    @NotBlank
    @Size(min=3, max=100)
    private String titre;

    @Min(1)
    @Max(3)
    private int priorite;

    private LocalDateTime dateCreation;
    @NotNull
    @Future
    private LocalDateTime dateLimite;

    @ManyToOne
    @JoinColumn(name = "id_statut")
    private Statut statut;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

}