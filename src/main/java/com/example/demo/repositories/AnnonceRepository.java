package com.example.demo.repositories;

import com.example.demo.entities.Annonce;
import com.example.demo.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long> {

    // Permet de filtrer les annonces par ville (utile pour la recherche)
    List<Annonce> findByVille(String ville);

    List<Annonce> findByEstVenduTrueOrderByIdDesc();
    List<Annonce> findTop10ByEstVenduTrueOrderByIdDesc();
    List<Annonce> findByUtilisateurId(Long id);
    List<Annonce> findByUtilisateur(Utilisateur utilisateur);
    List<Annonce> findByCategorie(String categorie);
    List<Annonce> findByTitreContainingIgnoreCase(String keyword);
}