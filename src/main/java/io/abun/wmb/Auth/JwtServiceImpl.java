package io.abun.wmb.Auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.abun.wmb.Auth.dto.JwtClaims;
import io.abun.wmb.Auth.interfaces.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final String JWT_SECRET;
    private final String ISSUER;
    private final long JWT_EXPIRATION;

    public JwtServiceImpl(
            @Value("${wmb.jwt.secret_key}") String jwtSecret,
            @Value("${wmb.jwt.issuer}") String issuer,
            @Value("${wmb.jwt.expirationInSecond}") long expiration
    ) {
        JWT_SECRET = jwtSecret;
        ISSUER = issuer;
        JWT_EXPIRATION = expiration;
    }

    @Override
    public String generateToken(UserAccountEntity userAccount) {
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
    public boolean verifyJwtToken(String bearerToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();

            String token = parseJwt(bearerToken);
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error("JWT verification error : {}", e.getMessage());
            return false;
        }
    }

    @Override
    public JwtClaims getClaimsByToken(String bearerToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();

            String token = parseJwt(bearerToken);
            DecodedJWT decodedJWT = verifier.verify(token);
            return new JwtClaims(decodedJWT.getSubject(), decodedJWT.getClaim("roles").asList(String.class));
        } catch (JWTVerificationException e) {
            log.error("JWT decode error : {}", e.getMessage());
            return null;
        }
    }

    private String parseJwt(String rawBearerToken) {
        if (rawBearerToken == null) {
            return null;
        } else if (!rawBearerToken.startsWith("Bearer ")) {
            return rawBearerToken;
        } else {
            return rawBearerToken.substring(7);
        }
    }
}
