package com.example.demo.controllers;

import com.example.demo.entities.Annonce;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repositories.AnnonceRepository;
import com.example.demo.repositories.UtilisateurRepository;
import com.example.demo.services.AnnonceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Controller
public class AnnonceController {

    private final AnnonceService annonceService;
    private final AnnonceRepository annonceRepository;
    private final UtilisateurRepository utilisateurRepository;

    public AnnonceController(AnnonceService annonceService, 
                             AnnonceRepository annonceRepository, 
                             UtilisateurRepository utilisateurRepository) {
        this.annonceService = annonceService;
        this.annonceRepository = annonceRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    // --- ACTION : MARQUER COMME VENDU ---
    @PostMapping("/annonce/vendre/{id}")
    public String marquerCommeVendu(@PathVariable Long id) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce introuvable"));
        
        annonce.setEstVendu(true); 
        annonceRepository.save(annonce);
        
        // Redirection vers le Cercle pour voir la success story
        return "redirect:/cercle"; 
    }

    // --- ACCUEIL ---
    @GetMapping("/")
    public String accueil(Model model, 
                         @RequestParam(name = "cat", required = false) String categorie,
                         @RequestParam(name = "keyword", required = false) String keyword) {
        List<Annonce> listeAnnonces;
        if (keyword != null && !keyword.isEmpty()) {
            listeAnnonces = annonceRepository.findByTitreContainingIgnoreCase(keyword);
        } else if (categorie != null && !categorie.isEmpty()) {
            listeAnnonces = annonceRepository.findByCategorie(categorie);
        } else {
            listeAnnonces = annonceRepository.findAll();
        }
        model.addAttribute("annonces", listeAnnonces);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    // --- DÉPOSER ANNONCE ---
    @GetMapping("/annonce/nouveau")
    public String afficherFormulaire(Model model, HttpSession session) {
        if (session.getAttribute("userConnecte") == null) return "redirect:/login"; 
        model.addAttribute("annonce", new Annonce());
        return "deposer-annonce";
    }

    @PostMapping("/deposer-annonce")
    public String enregistrerAnnonce(@ModelAttribute Annonce annonce, 
                                     @RequestParam("imageFile") MultipartFile file, 
                                     @RequestParam("telephone") String telephone,
                                     HttpSession session) throws IOException {
        Utilisateur userLogue = (Utilisateur) session.getAttribute("userConnecte");
        if (userLogue != null) {
            userLogue.setTelephone(telephone);
            utilisateurRepository.save(userLogue);

            if (file != null && !file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/uploads/");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                }
                annonce.setImageNom(fileName);
            }
            annonce.setUtilisateur(userLogue);
            annonceRepository.save(annonce);
            return "redirect:/";
        }
        return "redirect:/login";
    }

    // --- DÉTAILS & PROFIL ---
    @GetMapping("/annonce/{id}")
    public String voirAnnonce(@PathVariable Long id, Model model) {
        annonceRepository.findById(id).ifPresent(a -> model.addAttribute("annonce", a));
        return "details-annonce";
    }

    @GetMapping("/profil/{userId}")
    public String voirProfilVendeur(@PathVariable Long userId, Model model) {
        utilisateurRepository.findById(userId).ifPresent(v -> {
            model.addAttribute("vendeur", v);
            model.addAttribute("annonces", annonceRepository.findByUtilisateurId(userId));
        });
        return "profil-vendeur";
    }

    // --- SUPPRESSION & MODIFICATION ---
    @GetMapping("/annonce/supprimer/{id}")
    public String supprimerAnnonce(@PathVariable Long id, HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("userConnecte");
        annonceRepository.findById(id).ifPresent(ann -> {
            if (user != null && ann.getUtilisateur().getId().equals(user.getId())) {
                annonceRepository.delete(ann);
            }
        });
        return "redirect:/";
    }
}