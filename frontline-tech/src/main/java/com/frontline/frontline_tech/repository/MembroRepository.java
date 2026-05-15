package com.frontline.frontline_tech.repository;

import com.frontline.frontline_tech.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // <-- Precisamos desta importação de volta!

@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {
    
    // O método antigo que o sistema já usava (NÃO PODEMOS APAGAR)
    Optional<Membro> findByNome(String nome);

    // Os novos métodos para listar em ordem alfabética
    List<Membro> findAllByOrderByNomeAsc();

    // O novo método para pesquisar e ordenar
    List<Membro> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);
}
