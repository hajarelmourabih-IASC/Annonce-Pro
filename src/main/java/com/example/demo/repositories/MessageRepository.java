package com.example.demo.repositories;

import com.example.demo.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Pour le dashboard Admin : tous les messages du plus récent au plus ancien
    List<Message> findAllByOrderByDateEnvoiDesc();
    List<Message> findTop50BySujetIsNullOrderByDateEnvoiAsc();
    // Pour le chat global : les 50 derniers messages
    List<Message> findTop50ByOrderByDateEnvoiAsc();
    List<Message> findByEmailOrderByDateEnvoiAsc(String email);
    // SI tu as besoin de chercher par email, on passe par la relation 'auteur'
    // C'est "AuteurEmail" (Auteur = l'objet, Email = le champ dans l'objet)
    List<Message> findByAuteurEmailOrderByDateEnvoiDesc(String email);
}