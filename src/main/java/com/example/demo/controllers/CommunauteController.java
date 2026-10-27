package com.example.demo.controllers;

import com.example.demo.entities.Message;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repositories.AnnonceRepository;
import com.example.demo.repositories.MessageRepository;
import com.example.demo.repositories.UtilisateurRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class CommunauteController {

    private final UtilisateurRepository utilisateurRepository;
    private final AnnonceRepository annonceRepository;
    private final MessageRepository messageRepository;

    public CommunauteController(UtilisateurRepository utilisateurRepository, 
                                AnnonceRepository annonceRepository, 
                                MessageRepository messageRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.annonceRepository = annonceRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/cercle")
    public String afficherLeCercle(HttpSession session, Model model) {
        Utilisateur userLogue = (Utilisateur) session.getAttribute("userConnecte");
        if (userLogue == null) return "redirect:/login";

        model.addAttribute("membres", utilisateurRepository.findAll());
        model.addAttribute("stories", annonceRepository.findTop10ByEstVenduTrueOrderByIdDesc());
        model.addAttribute("chatHistory", messageRepository.findTop50BySujetIsNullOrderByDateEnvoiAsc());

        return "communaute"; 
    }

    @PostMapping("/api/chat/envoyer")
    @ResponseBody
    public String envoyerMessage(@RequestParam("texte") String texte, HttpSession session) {
        Utilisateur auteur = (Utilisateur) session.getAttribute("userConnecte");
        if (auteur != null && texte != null && !texte.trim().isEmpty()) {
            Message msg = new Message();
            msg.setContenu(texte);
            msg.setNom(auteur.getNom());
            msg.setDateEnvoi(LocalDateTime.now());
            msg.setAuteur(auteur);
            messageRepository.save(msg);
            return "SUCCESS";
        }
        return "ERROR";
    }

    @PostMapping("/api/user/update-mood")
    @ResponseBody
    public String changerMood(@RequestParam("mood") String mood, HttpSession session) {
        Utilisateur userSession = (Utilisateur) session.getAttribute("userConnecte");
        if (userSession != null) {
            Utilisateur userDb = utilisateurRepository.findById(userSession.getId()).orElse(null);
            if (userDb != null) {
                userDb.setMood(mood);
                utilisateurRepository.save(userDb);
                session.setAttribute("userConnecte", userDb);
                return "MOOD_OK";
            }
        }
        return "ERROR";
    }
}