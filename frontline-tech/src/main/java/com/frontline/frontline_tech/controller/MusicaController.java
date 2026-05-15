package com.frontline.frontline_tech.controller;

import com.frontline.frontline_tech.model.musica;
import com.frontline.frontline_tech.repository.MusicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Diz ao Spring que esta classe vai responder requisições da internet
@RequestMapping("/api/musicas") // É o "endereço" (URL) base dessa porta de entrada
@CrossOrigin(origins = "*") // MÁGICA: Impede que o navegador bloqueie o seu site de acessar a API
public class MusicaController {

    @Autowired
    private MusicaRepository musicaRepository; // Injeta o banco de dados que você criou antes

    // 1. Método GET: Lista todas as músicas cadastradas
    // O site dos jovens vai chamar esse método para montar a lista na tela
    @GetMapping
    public List<musica> listarMusicas() {
        return musicaRepository.findAll(); // Vai no MySQL e traz tudo
    }

    // 3. Método GET específico para o Ranking
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

    // 4. Método DELETE para zerar o ranking (Sincronizado com o JS)
    @DeleteMapping("/ranking/zerar")
    public void zerarRanking() {
        musicaRepository.deleteAll();
    }

    // NOVO: Método para excluir uma música específica pelo ID
    @DeleteMapping("/{id}")
    public void excluirMusica(@PathVariable Long id) {
        musicaRepository.deleteById(id);
    }
    // 2. Método POST: Cadastra uma nova música
    // Quando o jovem pesquisar um louvor e não achar, o site manda os dados pra cá
    @PostMapping
    public musica adicionarMusica(@RequestBody musica novaMusica) {

        // 1. Limpeza: Tira espaços em branco do começo e do fim que o usuário digitou sem querer
        String tituloLimpo = novaMusica.getTitulo().trim();
        novaMusica.setTitulo(tituloLimpo);

        // 2. Procura no banco ignorando maiúsculas/minúsculas (o IgnoreCase já faz a mágica)
        musica musicaExistente = musicaRepository.findByTituloIgnoreCase(tituloLimpo);

        if (musicaExistente != null) {
            int contagemAtual = musicaExistente.getQuantidadeSugestoes() != null ? musicaExistente.getQuantidadeSugestoes() : 0;
            musicaExistente.setQuantidadeSugestoes(contagemAtual + 1);

            return musicaRepository.save(musicaExistente);
        } else {
            novaMusica.setQuantidadeSugestoes(1);
            return musicaRepository.save(novaMusica);
        }
    }
}