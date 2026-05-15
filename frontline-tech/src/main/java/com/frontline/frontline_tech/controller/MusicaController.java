package com.frontline.frontline_tech.controller;

import com.frontline.frontline_tech.model.musica;
import com.frontline.frontline_tech.repository.MusicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/musicas")
@CrossOrigin(origins = "*")
public class MusicaController {

    @Autowired
    private MusicaRepository musicaRepository;

    @GetMapping
    public List<musica> listarMusicas() {
        return musicaRepository.findAll();
    }

    @GetMapping("/ranking")
    public List<musica> obterRanking() {
        return musicaRepository.findAll().stream()
                .sorted((m1, m2) -> {
                    int votos1 = m1.getQuantidadeSugestoes() != null ? m1.getQuantidadeSugestoes() : 0;
                    int votos2 = m2.getQuantidadeSugestoes() != null ? m2.getQuantidadeSugestoes() : 0;
                    return Integer.compare(votos2, votos1);
                })
                .toList();
    }

    @DeleteMapping("/ranking/zerar")
    public void zerarRanking() {
        musicaRepository.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void excluirMusica(@PathVariable Long id) {
        musicaRepository.deleteById(id);
    }

    // Usando ResponseEntity para poder avisar o site caso dê erro no Banco de Dados
    @PostMapping
    public ResponseEntity<?> adicionarMusica(@RequestBody musica novaMusica) {
        try {
            String tituloLimpo = novaMusica.getTitulo().trim();
            novaMusica.setTitulo(tituloLimpo);

            musica musicaExistente = musicaRepository.findByTituloIgnoreCase(tituloLimpo);

            if (musicaExistente != null) {
                int contagemAtual = musicaExistente.getQuantidadeSugestoes() != null ? musicaExistente.getQuantidadeSugestoes() : 0;
                musicaExistente.setQuantidadeSugestoes(contagemAtual + 1);
                musica salva = musicaRepository.save(musicaExistente);
                return ResponseEntity.ok(salva);
            } else {
                novaMusica.setQuantidadeSugestoes(1);
                musica salva = musicaRepository.save(novaMusica);
                return ResponseEntity.ok(salva);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Vai cuspir o erro real na tela preta do Render!
            return ResponseEntity.status(500).body("Erro no banco de dados: " + e.getMessage());
        }
    }
}
