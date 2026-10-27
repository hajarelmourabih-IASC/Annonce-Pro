package com.example.demo.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String ville;
    private String telephone; 
    private String mood;
    // --- CHAMPS PROFESSIONNELS ---
    private String photoProfil; 
    private LocalDate dateInscription = LocalDate.now();
    private int vuesTotales = 0; 
    private double score = 10.0; 
    private String témoignage; // L'avis de l'utilisateur sur l'app

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Annonce> annonces = new ArrayList<>();

    public Utilisateur() {}

    // --- LOGIQUE MÉTIER ---
    public boolean isCertifie() {
        return this.score >= 8.0 && this.annonces.size() >= 3;
    }

    // --- GETTERS ET SETTERS (TOUS COMPLETS) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getPhotoProfil() { return photoProfil; }
    public void setPhotoProfil(String photoProfil) { this.photoProfil = photoProfil; }

    public LocalDate getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDate dateInscription) { this.dateInscription = dateInscription; }

    public int getVuesTotales() { return vuesTotales; }
    public void setVuesTotales(int vuesTotales) { this.vuesTotales = vuesTotales; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getTémoignage() { return témoignage; }
    public void setTémoignage(String témoignage) { this.témoignage = témoignage; }
    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
    public List<Annonce> getAnnonces() { return annonces; }
    public void setAnnonces(List<Annonce> annonces) { this.annonces = annonces; }
}