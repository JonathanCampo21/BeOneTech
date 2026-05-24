package com.frontline.frontline_tech.controller;

import com.frontline.frontline_tech.model.Membro;
import com.frontline.frontline_tech.repository.MembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate; // <-- IMPORT NOVO PARA A MARRETA
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membros")
@CrossOrigin(origins = "*")
public class MembroController {

    @Autowired
    private MembroRepository membroRepository;

    // =========================================================
    // ---> FERRAMENTA SECRETA PARA FORÇAR A CRIAÇÃO DA TABELA <---
    // =========================================================
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/setup-db")
    public ResponseEntity<String> setupBancoDeDados() {
        try {
            // Dá a ordem direta pro banco de dados em SQL puro (AGORA COM PRIMARY KEY!)
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS membro_departamentos (membro_id BIGINT NOT NULL, departamento VARCHAR(255), PRIMARY KEY (membro_id, departamento))");
            return ResponseEntity.ok("Tabela 'membro_departamentos' forçada e criada com sucesso! Pode testar o login.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao marretar o banco: " + e.getMessage());
        }
    }
    // =========================================================

    // ATUALIZADO: Agora aceita o filtro por departamento do Front-end
    @GetMapping
    public List<Membro> listarMembros(@RequestParam(required = false) String departamento) {
        if (departamento != null && !departamento.trim().isEmpty()) {
            return membroRepository.findByDepartamentosContainingOrderByNomeAsc(departamento);
        }
        // Se não pedir departamento (ex: o Pastor), devolve todo mundo em ordem alfabética
        return membroRepository.findAllByOrderByNomeAsc();
    }

    @PostMapping
    public Membro adicionarMembro(@RequestBody Membro novoMembro) {
        return membroRepository.save(novoMembro);
    }

    @DeleteMapping("/{id}")
    public void excluirMembro(@PathVariable Long id) {
        membroRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Membro> atualizarMembro(@PathVariable Long id, @RequestBody Membro membroAtualizado) {
        return membroRepository.findById(id)
                .map(membro -> {
                    membro.setNome(membroAtualizado.getNome());
                    membro.setFuncao(membroAtualizado.getFuncao());
                    membro.setCargo(membroAtualizado.getCargo());
                    membro.setWhatsapp(membroAtualizado.getWhatsapp());

                    // ---> NOVO: Atualiza a lista de departamentos que o membro faz parte
                    membro.setDepartamentos(membroAtualizado.getDepartamentos());

                    // SALVANDO A FOTO QUE VEIO DA TELA
                    membro.setFotoPerfil(membroAtualizado.getFotoPerfil());

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
