package com.example.demo.controllers;

import com.example.demo.entities.Message;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repositories.MessageRepository;
import com.example.demo.repositories.UtilisateurRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepository;
    private final MessageRepository messageRepository;

    public UtilisateurController(UtilisateurRepository utilisateurRepository, MessageRepository messageRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.messageRepository = messageRepository;
    }

    // --- NOUVEAU : AFFICHAGE DE LA PAGE CONTACT (Noir & Or) ---
    @GetMapping("/contact")
    public String afficherContact(HttpSession session, Model model) {
        Utilisateur userLogue = (Utilisateur) session.getAttribute("userConnecte");
        if (userLogue == null) return "redirect:/login";

        // Récupérer l'historique des messages de l'utilisateur pour l'afficher en bas de page
        List<Message> mesMessages = messageRepository.findByEmailOrderByDateEnvoiAsc(userLogue.getEmail());
        
        model.addAttribute("messages", mesMessages);
        model.addAttribute("user", userLogue);
        return "contact"; 
    }

    // --- NOUVEAU : ENVOI DU MESSAGE VERS L'ADMIN ---
    @PostMapping("/contact/envoyer")
    public String envoyerMessageVersAdmin(@RequestParam("sujet") String sujet, 
                                         @RequestParam("contenu") String contenu, 
                                         HttpSession session) {
        Utilisateur userLogue = (Utilisateur) session.getAttribute("userConnecte");
        if (userLogue == null) return "redirect:/login";

        Message msg = new Message();
        msg.setSujet(sujet);
        msg.setContenu(contenu);
        msg.setNom(userLogue.getNom());
        msg.setEmail(userLogue.getEmail());
        msg.setDateEnvoi(LocalDateTime.now());
        
        messageRepository.save(msg);
        return "redirect:/contact?success";
    }

    
    @GetMapping("/contact/supprimer/{id}")
    public String supprimerMessage(@PathVariable Long id, HttpSession session) {
        Utilisateur userLogue = (Utilisateur) session.getAttribute("userConnecte");
        if (userLogue == null) return "redirect:/login";

        // Sécurité : On vérifie que le message appartient bien à l'utilisateur connecté
        Optional<Message> msg = messageRepository.findById(id);
        if (msg.isPresent() && msg.get().getEmail().equals(userLogue.getEmail())) {
            messageRepository.deleteById(id);
        }
        
        return "redirect:/contact?deleted";
    }
    // --- AUTHENTIFICATION (INCHANGÉ) ---

    @GetMapping("/login")
    public String afficherLogin() { return "login"; }

    @PostMapping("/login")
    public String traiterLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute("userConnecte", user.get());
            return "redirect:/";
        }
        model.addAttribute("error", "Email ou mot de passe incorrect.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // --- PROFIL (INCHANGÉ) ---

    @GetMapping("/mon-profil")
    public String voirMonProfil(HttpSession session, Model model) {
        Utilisateur userLogue = (Utilisateur) session.getAttribute("userConnecte");
        if (userLogue == null) return "redirect:/login";
        
        Utilisateur freshUser = utilisateurRepository.findById(userLogue.getId()).orElseThrow();
        model.addAttribute("user", freshUser);
        model.addAttribute("annonces", freshUser.getAnnonces());
        return "mon-profil"; 
    }
    @PostMapping("/update-mood")
    @ResponseBody 
    public String updateMood(@RequestParam("mood") String mood, HttpSession session) {
        Utilisateur userLogue = (Utilisateur) session.getAttribute("userConnecte");
        
        if (userLogue != null) {
            // 1. Récupérer l'utilisateur frais depuis la DB pour éviter les erreurs de détachement Hibernate
            Utilisateur userDb = utilisateurRepository.findById(userLogue.getId()).orElseThrow();
            
            // 2. Mettre à jour le mood
            userDb.setMood(mood);
            
            // 3. Sauvegarder en base
            utilisateurRepository.save(userDb);
            
            // 4. METTRE À JOUR LA SESSION (Très important pour l'affichage immédiat)
            session.setAttribute("userConnecte", userDb);
            
            return "Mood " + mood + " enregistré !";
        }
        return "Erreur : Utilisateur non connecté";
    }
}