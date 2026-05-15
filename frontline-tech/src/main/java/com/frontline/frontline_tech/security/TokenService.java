package com.frontline.frontline_tech.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.frontline.frontline_tech.model.Membro;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Membro membro) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("BeOneTech")
                    .withSubject(membro.getNome()) // Quem está logando
                    .withClaim("cargo", membro.getCargo()) // Guarda o cargo no crachá
                    .withExpiresAt(dataExpiracao()) // Duração do token
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // ADICIONE ESTE MÉTODO NO SEU TokenService.java
    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("BeOneTech")
                    .build()
                    .verify(token)
                    .getSubject(); // Se for válido, devolve o nome do membro
        } catch (Exception exception) {
            return ""; // Se o token for falso, alterado ou vencido, devolve vazio e a catraca trava
        }
    }

    private Instant dataExpiracao() {
        // O Token vai durar 12 horas
        return LocalDateTime.now().plusHours(12).toInstant(ZoneOffset.of("-03:00"));
    }
}