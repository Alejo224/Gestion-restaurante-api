package com.backend.sistemarestaurante.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    // Aquí puedes agregar métodos para generar y validar tokens JWT
    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    // metodos de uteleria
    public String createToken(Authentication authentication) {
        // Definir el algoritmo de encriptacion
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        // Extraer el usuario que se va a autenticar
        String username = authentication.getPrincipal().toString();

        // Obtner los atorizaciones
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); //READ,WRITE ...

        // Generar el token
        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() +  1800000)) // 30 minutos
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);

        return jwtToken;
    }

    // metodo para validar el token JWT
    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;

        } catch (JWTVerificationException exception){
        throw new JWTVerificationException("Token invalid, not Authorized");
        }
    }

    // metodo para extraer el username del token
    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject();
    }

    // Extraer el claim
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName){
        return decodedJWT.getClaim(claimName);
    }

    // Devolver todos los claims
    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }
}
