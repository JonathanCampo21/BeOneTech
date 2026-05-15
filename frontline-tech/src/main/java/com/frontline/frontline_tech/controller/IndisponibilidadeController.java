package com.frontline.frontline_tech.controller;

import com.frontline.frontline_tech.model.Indisponibilidade;
import com.frontline.frontline_tech.repository.IndisponibilidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indisponibilidade") // O endereço exato que o JS está procurando!
@CrossOrigin(origins = "*")
public class IndisponibilidadeController {

    @Autowired
    private IndisponibilidadeRepository repo;

    // Lista TODAS as faltas do sistema
    @GetMapping
    public List<Indisponibilidade> listarTodas() {
        return repo.findAll();
    }

    // Busca quem não pode em uma data específica
    @GetMapping("/data/{data}")
    public List<Indisponibilidade> buscarPorData(@PathVariable String data) {
        return repo.findByData(data);
    }

    // Salva a data que o membro informou
    @PostMapping
    public Indisponibilidade salvar(@RequestBody Indisponibilidade ind) {
        return repo.save(ind);
    }

    // Deleta caso o membro mude de ideia e decida ir
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}