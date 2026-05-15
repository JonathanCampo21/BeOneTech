package com.frontline.frontline_tech.repository;

import com.frontline.frontline_tech.model.Indisponibilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndisponibilidadeRepository extends JpaRepository<Indisponibilidade, Long> {

    // Mágica: O Java vai buscar todas as faltas de um dia específico!
    List<Indisponibilidade> findByData(String data);

    // Mágica: O Java vai buscar todos os dias que um membro específico marcou
    List<Indisponibilidade> findByNomeMembro(String nomeMembro);
}