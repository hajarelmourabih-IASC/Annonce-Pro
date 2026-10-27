package com.example.demo.services;

import com.example.demo.entities.Annonce;
import com.example.demo.repositories.AnnonceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnnonceService {

    private final AnnonceRepository annonceRepository;

    public AnnonceService(AnnonceRepository annonceRepository) {
        this.annonceRepository = annonceRepository;
    }

    public List<Annonce> getAll() {
        return annonceRepository.findAll();
    }

    public List<Annonce> getByVille(String ville) {
        return annonceRepository.findByVille(ville);
    }

    public Annonce save(Annonce annonce) {
        return annonceRepository.save(annonce);
    }
    public Annonce getById(Long id) {
        return annonceRepository.findById(id).orElse(null);
    }
    public List<Annonce> getByCategorie(String cat) {
        return annonceRepository.findByCategorie(cat);
    }
}