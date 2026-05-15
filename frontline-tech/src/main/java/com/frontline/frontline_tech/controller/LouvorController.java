package com.frontline.frontline_tech.controller;

import com.frontline.frontline_tech.model.Louvor;
import com.frontline.frontline_tech.repository.LouvorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/louvores")
@CrossOrigin(origins = "*")
public class LouvorController {

    @Autowired
    private LouvorRepository louvorRepository;

    @GetMapping
    public List<Louvor> listarLouvores() {
        return louvorRepository.findAll();
    }

    @PostMapping
    public Louvor adicionarLouvor(@RequestBody Louvor novoLouvor) {
        return louvorRepository.save(novoLouvor);
    }

    @DeleteMapping("/{id}")
    public void excluirLouvor(@PathVariable Long id) {
        louvorRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Louvor> atualizarLouvor(@PathVariable Long id, @RequestBody Louvor louvorAtualizado) {
        return louvorRepository.findById(id)
                .map(louvor -> {
                    louvor.setTitulo(louvorAtualizado.getTitulo());
                    louvor.setTom(louvorAtualizado.getTom());
                    louvor.setArtista(louvorAtualizado.getArtista());

                    louvor.setBpm(louvorAtualizado.getBpm());
                    louvor.setCompasso(louvorAtualizado.getCompasso()); // SALVA COMPASSO
                    louvor.setImagemUrl(louvorAtualizado.getImagemUrl());
                    louvor.setLinkCifra(louvorAtualizado.getLinkCifra());
                    louvor.setLinkLetra(louvorAtualizado.getLinkLetra());
                    louvor.setLinkVs(louvorAtualizado.getLinkVs());
                    louvor.setLinkYoutube(louvorAtualizado.getLinkYoutube()); // SALVA YOUTUBE

                    Louvor salvo = louvorRepository.save(louvor);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}