package com.frontline.frontline_tech.repository;

import com.frontline.frontline_tech.model.Escala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EscalaRepository extends JpaRepository<Escala, Long> {
    
    // MÁGICA: O Spring Boot cria a consulta no banco sozinho pra filtrar pelo departamento!
    List<Escala> findByDepartamento(String departamento);
}
