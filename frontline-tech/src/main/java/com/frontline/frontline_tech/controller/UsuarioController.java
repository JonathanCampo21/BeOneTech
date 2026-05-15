package com.frontline.frontline_tech.controller;

import com.frontline.frontline_tech.model.Usuario;
import com.frontline.frontline_tech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario criarUsuario(@RequestBody Usuario novoUsuario) {
        return usuarioRepository.save(novoUsuario);
    }

    @DeleteMapping("/{id}")
    public void excluirUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }

    // --- O NOSSO LEÃO DE CHÁCARA ---
    @PostMapping("/login")
    public ResponseEntity<Usuario> fazerLogin(@RequestBody Usuario tentativaLogin) {
        // Verifica se os dados chegaram antes de chamar o banco
        if (tentativaLogin.getLogin() == null || tentativaLogin.getSenha() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Usuario usuarioEncontrado = usuarioRepository.findByLoginAndSenha(
                tentativaLogin.getLogin().trim(),
                tentativaLogin.getSenha().trim()
        );

        if (usuarioEncontrado != null) {
            return ResponseEntity.ok(usuarioEncontrado);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}