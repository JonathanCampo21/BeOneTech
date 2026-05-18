package com.frontline.frontline_tech.security;

import com.frontline.frontline_tech.model.Usuario; // <--- Import do modelo
import com.frontline.frontline_tech.repository.UsuarioRepository; // <--- Import do seu repositório
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime; // <--- Import do tempo
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository; // <--- Injetamos o banco de dados aqui!

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Pega o token do cabeçalho que o auth.js enviou
        var token = recuperarToken(request);

        if (token != null) {
            // 2. Valida o token e pega o login do usuário
            var subject = tokenService.validarToken(token);

            if (!subject.isEmpty()) {
                // 3. Avisa o Spring Security: "Opa, esse cara tem crachá válido, pode deixar passar!"
                var authentication = new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // ---> INÍCIO DA MÁGICA DO RADAR <---
                // Procura o cara no banco de dados usando o login dele (que veio do token)
                Usuario usuarioLogado = usuarioRepository.findByNome(subject); // Se o seu subject do JWT for o 'login' ao invés do 'nome', mude para findByLogin(subject)
                
                if (usuarioLogado != null) {
                    usuarioLogado.setUltimaAtividade(LocalDateTime.now()); // Carimba a hora exata
                    usuarioRepository.save(usuarioLogado); // Salva no banco silenciosamente!
                }
                // ---> FIM DA MÁGICA DO RADAR <---
            }
        }

        // 4. Continua a requisição para a catraca do SecurityConfig
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", ""); // Tira a palavra Bearer e deixa só o código
    }
}
