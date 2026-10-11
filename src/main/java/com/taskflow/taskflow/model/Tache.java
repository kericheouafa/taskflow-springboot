package com.taskflow.taskflow.model;

import jakarta.persistence.*;
import java.time.LocalDate;

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
    private LocalDate dateCreation;
    @NotNull
    @Future
    private LocalDate dateLimite;
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

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateLimite(LocalDate dateLimite) {
        this.dateLimite = dateLimite;
    }

    public LocalDate getDateLimite() {
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