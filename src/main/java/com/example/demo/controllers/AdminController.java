package com.example.demo.controllers;

import com.example.demo.entities.Utilisateur;
import com.example.demo.entities.Message;
import com.example.demo.repositories.AnnonceRepository;
import com.example.demo.repositories.UtilisateurRepository;
import com.example.demo.repositories.MessageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    private final AnnonceRepository annonceRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final MessageRepository messageRepository;

    public AdminController(AnnonceRepository annonceRepository, 
                           UtilisateurRepository utilisateurRepository,
                           MessageRepository messageRepository) {
        this.annonceRepository = annonceRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/admin-dashboard")
    public String dashboard(HttpSession session, Model model) {
        Utilisateur user = (Utilisateur) session.getAttribute("userConnecte");

        // Vérification de sécurité (Admin spécifique)
        if (user == null || !user.getEmail().equalsIgnoreCase("hajar@gmail.com")) {
            return "redirect:/login"; // Rediriger vers login plutôt que l'accueil
        }

        // Statistiques
        model.addAttribute("totalAnnonces", annonceRepository.count());
        model.addAttribute("totalUsers", utilisateurRepository.count());
        model.addAttribute("totalMessages", messageRepository.count());

        // Données des tables
        model.addAttribute("allAnnonces", annonceRepository.findAll());
        model.addAttribute("allUsers", utilisateurRepository.findAll());
        
        // CORRECTION ICI : Utiliser la méthode de tri que nous avons créée dans le Repository
        model.addAttribute("allMessages", messageRepository.findAllByOrderByDateEnvoiDesc()); 

        // Graphique (Données factices pour l'exemple)
        List<String> jours = Arrays.asList("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim");
        List<Integer> statsAnnonces = Arrays.asList(5, 12, 8, 20, 15, 25, 30);
        
        model.addAttribute("labelsGraph", jours);
        model.addAttribute("dataGraph", statsAnnonces);

        return "admin-dashboard"; 
    }

    @PostMapping("/admin/repondre-interne")
    public String repondreInterne(@RequestParam("messageId") Long id, 
                                 @RequestParam("texteReponse") String texte, 
                                 HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("userConnecte");
        
        if (user != null && user.getEmail().equalsIgnoreCase("hajar@gmail.com")) {
            Optional<Message> optMsg = messageRepository.findById(id);
            if (optMsg.isPresent()) {
                Message msg = optMsg.get();
                msg.setReponse(texte);
                msg.setDateReponse(LocalDateTime.now());
                msg.setLuParUtilisateur(false);
                messageRepository.save(msg);
            }
        }
        return "redirect:/admin-dashboard?success";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteAnnonce(@PathVariable Long id, HttpSession session) {
        if (isAdmin(session)) {
            annonceRepository.deleteById(id);
        }
        return "redirect:/admin-dashboard";
    }

    @GetMapping("/admin/delete-user/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (isAdmin(session)) {
            utilisateurRepository.deleteById(id);
        }
        return "redirect:/admin-dashboard";
    }

    @GetMapping("/admin/delete-message/{id}")
    public String deleteMessage(@PathVariable Long id, HttpSession session) {
        if (isAdmin(session)) {
            messageRepository.deleteById(id);
        }
        return "redirect:/admin-dashboard";
    }

    // Petite méthode utilitaire pour éviter la répétition du test email
    private boolean isAdmin(HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("userConnecte");
        return user != null && user.getEmail().equalsIgnoreCase("hajar@gmail.com");
    }
}