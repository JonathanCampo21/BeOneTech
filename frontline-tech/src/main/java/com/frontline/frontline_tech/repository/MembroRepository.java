package com.frontline.frontline_tech.repository;

import com.frontline.frontline_tech.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {
    
    // Os métodos de busca precisam ficar AQUI DENTRO, antes da última chave
    
    // Para listar todos em ordem alfabética
    List<Membro> findAllByOrderByNomeAsc();

    // Para pesquisar por nome e já vir ordenado
    List<Membro> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

} // <-- O arquivo TEM que terminar com essa chave fechando a interface!
