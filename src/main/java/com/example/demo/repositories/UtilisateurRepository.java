package com.example.demo.repositories;

import com.example.demo.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    
    // Recherche un utilisateur par son email (pour la connexion)
    Optional<Utilisateur> findByEmail(String email);

    // --- CORRECTION POUR HIBERNATE 6 ---
    // On utilise "IS NOT EMPTY" pour récupérer les membres ayant au moins une annonce
    @Query("SELECT u FROM Utilisateur u WHERE u.annonces IS NOT EMPTY")
    List<Utilisateur> findActiveMembers();
}