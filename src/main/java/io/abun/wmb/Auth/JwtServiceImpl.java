package io.abun.wmb.Auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import io.abun.springbootikamers.Auth.dto.JwtClaims;
import io.abun.springbootikamers.Auth.interfaces.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

public class JwtServiceImpl implements JwtService {
    final String JWT_SECRET;
    final String ISSUER;
    final long JWT_EXPIRATION;

    public JwtServiceImpl(
            @Value("${enigma_shop.jwt.secret_key}") String jwtSecret,
            @Value("${enigma_shop.jwt.issuer}") String issuer,
            @Value("${enigma_shop.jwt.expirationInSecond}") long expiration
    ) {
        JWT_SECRET = jwtSecret;
        ISSUER = issuer;
        JWT_EXPIRATION = expiration;
    }

    @Override
    public String generateToken(UserAccount userAccount) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            return JWT.create()
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(JWT_EXPIRATION))
                    .withIssuer(ISSUER)
                    .withSubject(userAccount.getId().toString())
                    .withClaim("roles", userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating token");
        }
    }

    @Override
    public boolean verifyJwtToken(String token) {
        return false;
    }

    @Override
    public JwtClaims getClaimsByToken(String token) {
        return null;
    }
}
