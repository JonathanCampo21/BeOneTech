package com.frontline.frontline_tech.security;

import com.frontline.frontline_tech.model.Usuario;
import com.frontline.frontline_tech.repository.UsuarioRepository;
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

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository; 

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recuperarToken(request);

        if (token != null) {
            var subject = tokenService.validarToken(token);

            if (!subject.isEmpty()) {
                var authentication = new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // ---> AQUI ESTAVA O ERRO! TROCAMOS PARA findByLogin <---
                Usuario usuarioLogado = usuarioRepository.findByLogin(subject); 
                
                if (usuarioLogado != null) {
                    usuarioLogado.setUltimaAtividade(LocalDateTime.now()); 
                    usuarioRepository.save(usuarioLogado); 
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
