package com.frontline.frontline_tech.repository;

import com.frontline.frontline_tech.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // MÁGICA: O Spring Boot cria a consulta no banco de dados sozinho só de lermos o nome desse método!
    Usuario findByLoginAndSenha(String login, String senha);

    // ---> NOVO: Necessário para o radar encontrar o usuário apenas pelo login do Token JWT!
    Usuario findByLogin(String login);
}
