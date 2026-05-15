package com.frontline.frontline_tech.controller;

import com.frontline.frontline_tech.model.Membro;
import com.frontline.frontline_tech.repository.MembroRepository;
import com.frontline.frontline_tech.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private MembroRepository membroRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> dadosLogin) {
        String nome = dadosLogin.get("nome");
        String senha = dadosLogin.get("senha");

        // 1. Procura o membro pelo nome
        Optional<Membro> membroOpt = membroRepository.findByNome(nome); // Você vai precisar criar esse método no Repository!

        if (membroOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuário não encontrado!");
        }

        Membro membro = membroOpt.get();

        // 2. Verifica se a senha bate (Por enquanto, senha pura)
        if (!senha.equals(membro.getSenha())) {
            return ResponseEntity.status(401).body("Senha incorreta!");
        }

        // 3. Se tudo deu certo, gera o crachá (Token)
        String token = tokenService.gerarToken(membro);

        // Devolve o token e os dados do cara pro Front-end guardar no LocalStorage
        return ResponseEntity.ok(Map.of(
                "token", token,
                "nome", membro.getNome(),
                "cargo", membro.getCargo()
        ));
    }
}