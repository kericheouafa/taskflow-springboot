package com.taskflow.taskflow.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


// @Data                  pb lombok on va mettre getters et setters manuellement

@Entity
@Table(name = "utilisateur")
public class Utilisateur {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    @Column(nullable = false)
    private String email;
    private String role;
    private String motDePasse;
    private LocalDateTime dateCreation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
}
