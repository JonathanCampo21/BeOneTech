package com.frontline.frontline_tech.config;

import com.frontline.frontline_tech.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter; // <--- Injeta o nosso Segurança aqui!

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                }))
                // AQUI FICA A NOSSA NOVA CATRACA
                .authorizeHttpRequests(auth -> auth
                        // 1. TELA ABERTA: O Front-end não precisa de token para carregar visualmente
                        .requestMatchers("/", "/index.html", "/*.html", "/style.css", "/*.js", "/images/**", "/static/**").permitAll()

                        // 2. PORTA DO LOGIN: A rota de fazer login tem que ser pública, senão ninguém entra
                        .requestMatchers("/api/auth/login").permitAll()

                        // 3. A CATRACA: Daqui pra baixo, TUDO dentro de /api exige o Token!
                        .requestMatchers("/api/**").authenticated()

                        .anyRequest().permitAll()
                )

                // Avisa ao Spring que não vamos usar sessões antigas, só o nosso JWT Stateless
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Coloca o nosso Segurança NA FRENTE da porta
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}