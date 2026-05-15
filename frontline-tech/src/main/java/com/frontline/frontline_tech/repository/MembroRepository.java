package com.frontline.frontline_tech.repository;

import com.frontline.frontline_tech.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {
    Optional<Membro> findByNome(String nome);
    // Por enquanto, não precisamos de nenhuma busca especial aqui.
    // O JpaRepository já nos dá o "salvar", "deletar" e "listar todos" de graça!
}

