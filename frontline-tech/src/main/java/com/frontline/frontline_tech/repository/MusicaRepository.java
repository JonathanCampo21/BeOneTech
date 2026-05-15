package com.frontline.frontline_tech.repository;

import com.frontline.frontline_tech.model.musica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
    public interface MusicaRepository extends JpaRepository<musica, Long> {

        // Adicione esta linha:
        musica findByTituloIgnoreCase(String titulo);

        // O Spring Data JPA cria a lógica de ordenação sozinho apenas pelo nome do método!
        List<musica> findAllByOrderByQuantidadeSugestoesDesc();
    }