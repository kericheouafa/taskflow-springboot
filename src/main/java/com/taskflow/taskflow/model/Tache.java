package com.taskflow.taskflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;



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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateLimite(LocalDateTime dateLimite) {
        this.dateLimite = dateLimite;
    }

    public LocalDateTime getDateLimite() {
        return dateLimite;
    }

    public void setPriorite(int priorite) {
        this.priorite = priorite;
    }

    public int getPriorite() {
        return priorite;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTitre() {
        return titre;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
}