package com.frontline.frontline_tech.controller;

import com.frontline.frontline_tech.model.Membro;
import com.frontline.frontline_tech.repository.MembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membros")
@CrossOrigin(origins = "*")
public class MembroController {

    @Autowired
    private MembroRepository membroRepository;

    @GetMapping
    public List<Membro> listarMembros() {
        return membroRepository.findAll();
    }

    @PostMapping
    public Membro adicionarMembro(@RequestBody Membro novoMembro) {
        return membroRepository.save(novoMembro);
    }

    @DeleteMapping("/{id}")
    public void excluirMembro(@PathVariable Long id) {
        membroRepository.deleteById(id);
    }

    // ==========================================
    // NOVO: MÉTODO PARA EDITAR O MEMBRO
    // ==========================================
    @PutMapping("/{id}")
    public ResponseEntity<Membro> atualizarMembro(@PathVariable Long id, @RequestBody Membro membroAtualizado) {
        return membroRepository.findById(id)
                .map(membro -> {
                    membro.setNome(membroAtualizado.getNome());
                    membro.setFuncao(membroAtualizado.getFuncao());
                    membro.setCargo(membroAtualizado.getCargo());

                    // Atualiza a senha APENAS se o usuário digitou uma nova
                    if (membroAtualizado.getSenha() != null && !membroAtualizado.getSenha().isEmpty()) {
                        membro.setSenha(membroAtualizado.getSenha());
                    }

                    Membro salvo = membroRepository.save(membro);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}