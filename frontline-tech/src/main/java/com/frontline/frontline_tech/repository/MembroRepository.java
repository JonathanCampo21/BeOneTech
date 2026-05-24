package com.frontline.frontline_tech.repository;

import com.frontline.frontline_tech.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {
    
    // O método antigo que o sistema já usava (NÃO PODEMOS APAGAR)
    Optional<Membro> findByNome(String nome);

    // Os métodos para listar em ordem alfabética
    List<Membro> findAllByOrderByNomeAsc();

    // O método para pesquisar e ordenar
    List<Membro> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    // ---> NOVO: Busca apenas os membros que tenham o departamento especificado na lista deles!
    List<Membro> findByDepartamentosContainingOrderByNomeAsc(String departamento);
}
