package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "annonces")
public class Annonce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    
    @Column(length = 2000)
    private String description;
    
    private double prix;
    private String ville;
    private String categorie; 
    private String imageNom; 

    // --- AJOUT POUR LE SUCCESS WALL ---
    private boolean estVendu = false; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    public Annonce() {}

    // --- NOUVEAUX GETTER ET SETTER POUR estVendu ---
    public boolean isEstVendu() { return estVendu; }
    public void setEstVendu(boolean estVendu) { this.estVendu = estVendu; }

    // --- RESTE DES GETTERS ET SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public String getImageNom() { return imageNom; }
    public void setImageNom(String imageNom) { this.imageNom = imageNom; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
}