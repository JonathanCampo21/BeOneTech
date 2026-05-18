package com.frontline.frontline_tech.security;

import com.frontline.frontline_tech.model.Membro;
import com.frontline.frontline_tech.repository.MembroRepository;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MembroRepository membroRepository; // <-- Agora usamos o MembroRepository!

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recuperarToken(request);

        if (token != null) {
            var subject = tokenService.validarToken(token);

            if (!subject.isEmpty()) {
                var authentication = new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // ---> MÁGICA DO RADAR <---
                // Busca o Membro pelo nome (que é o subject salvo no seu Token)
                Optional<Membro> membroOpt = membroRepository.findByNome(subject);
                
                if (membroOpt.isPresent()) {
                    Membro membroLogado = membroOpt.get();
                    membroLogado.setUltimaAtividade(LocalDateTime.now()); // Carimba a hora
                    membroRepository.save(membroLogado); // Salva no banco!
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", ""); 
    }
}
