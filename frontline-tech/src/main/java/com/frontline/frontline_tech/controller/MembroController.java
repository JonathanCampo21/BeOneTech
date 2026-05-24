package com.frontline.frontline_tech.controller;

import com.frontline.frontline_tech.model.Membro;
import com.frontline.frontline_tech.repository.MembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/membros")
@CrossOrigin(origins = "*")
public class MembroController {

    @Autowired
    private MembroRepository membroRepository;

    // 1. LISTAR: Devolve APENAS os membros do departamento atual selecionado!
    @GetMapping
    public List<Membro> listarMembros(@RequestParam(required = false) String departamento) {
        if (departamento != null && !departamento.trim().isEmpty()) {
            return membroRepository.findByDepartamentosContainingOrderByNomeAsc(departamento);
        }
        // Se não passar departamento (Visão Geral do Pastor), traz a listagem completa
        return membroRepository.findAllByOrderByNomeAsc();
    }

    // 2. SCANNER / BUSCA POR NOME: Usado pelo front-end para verificar duplicidade global
    @GetMapping("/buscar-por-nome")
    public ResponseEntity<Membro> buscarPorNome(@RequestParam String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        String nomeFormatado = nome.trim();
        Optional<Membro> membroOpt = membroRepository.findAll().stream()
                .filter(m -> m.getNome() != null && m.getNome().trim().equalsIgnoreCase(nomeFormatado))
                .findFirst();
                
        return miembroOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. POST INTELLIGENT: Decide dinamicamente entre criar um membro do zero ou apenas vinculá-lo
    @PostMapping
    public ResponseEntity<Membro> adicionarMembro(@RequestBody Membro novoMembro, @RequestParam String departamentoAtual) {
        if (novoMembro.getNome() == null || novoMembro.getNome().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String nomeFormatado = novoMembro.getNome().trim();
        
        // Verifica se este irmão já possui uma ficha cadastrada em qualquer outro ministério
        Optional<Membro> membroExistenteOpt = membroRepository.findAll().stream()
                .filter(m -> m.getNome() != null && m.getNome().trim().equalsIgnoreCase(nomeFormatado))
                .findFirst();

        if (membroExistenteOpt.isPresent()) {
            // ---> FLUXO DE VÍNCULO: O irmão já existe no sistema, reaproveitaremos os dados dele!
            Membro membroExistente = membroExistenteOpt.get();
            
            // Instancia a lista caso venha nula do banco por segurança
            if (membroExistente.getDepartamentos() == null) {
                membroExistente.setDepartamentos(new ArrayList<>());
            }
            
            // Adiciona o novo departamento se ele já não fizer parte
            if (!membroExistente.getDepartamentos().contains(departamentoAtual)) {
                membroExistente.getDepartamentos().add(departamentoAtual);
            }
            
            // Mescla as funções separando por vírgula (Ex: "Som" + "Vocal" vira "Som, Vocal")
            if (novoMembro.getFuncao() != null && !novoMembro.getFuncao().trim().isEmpty()) {
                String novaFuncao = novoMembro.getFuncao().trim();
                if (membroExistente.getFuncao() == null || membroExistente.getFuncao().trim().isEmpty()) {
                    membroExistente.setFuncao(novaFuncao);
                } else if (!membroExistente.getFuncao().contains(novaFuncao)) {
                    membroExistente.setFuncao(membroExistente.getFuncao().trim() + ", " + novaFuncao);
                }
            }
            
            Membro membroVinculado = membroRepository.save(membroExistente);
            return ResponseEntity.ok(membroVinculado);
        } else {
            // ---> FLUXO DE CADASTRO ZERO: Novo registro na base de dados global
            novoMembro.setNome(nomeFormatado);
            
            if (novoMembro.getDepartamentos() == null) {
                novoMembro.setDepartamentos(new ArrayList<>());
            }
            if (!novoMembro.getDepartamentos().contains(departamentoAtual)) {
                novoMembro.getDepartamentos().add(departamentoAtual);
            }
            
            // Configurações e travas padrões para novos registros
            if (novoMembro.getSenha() == null || novoMembro.getSenha().isEmpty()) {
                novoMembro.setSenha("123"); 
            }
            if (novoMembro.getCargo() == null || novoMembro.getCargo().isEmpty()) {
                novoMembro.setCargo("MEMBRO");
            }
            
            Membro membroSalvo = membroRepository.save(novoMembro);
            return ResponseEntity.ok(membroSalvo);
        }
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
                    membro.setDepartamentos(membroAtualizado.getDepartamentos());
                    membro.setFotoPerfil(membroAtualizado.getFotoPerfil());

                    if (membroAtualizado.getSenha() != null && !membroAtualizado.getSenha().isEmpty()) {
                        membro.setSenha(membroAtualizado.getSenha());
                    }

                    Membro salvo = membroRepository.save(membro);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
