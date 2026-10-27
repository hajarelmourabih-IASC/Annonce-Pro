package com.example.demo.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Message {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ajoute ces colonnes explicitement pour le Chat
    private String nom;
    private String email;
    private String sujet; // Crucial pour filtrer Chat vs SAV

    @Column(length = 1000, nullable = false)
    private String contenu;
    
    private LocalDateTime dateEnvoi = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur auteur;

    @Column(length = 2000)
    private String reponse;
    private LocalDateTime dateReponse;
    private boolean luParUtilisateur = false;

    public Message() {}

    // --- LOGIQUE DES GETTERS/SETTERS CORRIGÉE ---

    public String getNom() {
        if (this.nom != null) return this.nom;
        return (auteur != null) ? auteur.getNom() : "Anonyme";
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        if (this.email != null) return this.email;
        return (auteur != null) ? auteur.getEmail() : "Inconnu";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    // --- AUTRES GETTERS ET SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    
    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
    
    public Utilisateur getAuteur() { return auteur; }
    public void setAuteur(Utilisateur auteur) { this.auteur = auteur; }
    
    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }
    
    public LocalDateTime getDateReponse() { return dateReponse; }
    public void setDateReponse(LocalDateTime dateReponse) { this.dateReponse = dateReponse; }
    
    public boolean isLuParUtilisateur() { return luParUtilisateur; }
    public void setLuParUtilisateur(boolean luParUtilisateur) { this.luParUtilisateur = luParUtilisateur; }
}