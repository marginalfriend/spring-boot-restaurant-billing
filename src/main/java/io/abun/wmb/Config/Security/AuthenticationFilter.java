package io.abun.wmb.Config.Security;

import io.abun.wmb.Auth.UserAccountEntity;
import io.abun.wmb.Auth.dto.JwtClaims;
import io.abun.wmb.Auth.interfaces.JwtService;
import io.abun.wmb.Auth.interfaces.UserAccountService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    final String AUTH_HEADER = "Authorization";
    private final JwtService jwtService;
    private final UserAccountService userAccountService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String bearerToken = request.getHeader(AUTH_HEADER);

            if (bearerToken != null && jwtService.verifyJwtToken(bearerToken)) {

                JwtClaims decodedJwt = jwtService.getClaimsByToken(bearerToken);
                log.info("Created decoded token : {}", decodedJwt);

                UserAccountEntity userAccount = userAccountService.loadUserById(UUID.fromString(decodedJwt.userAccountId()));
                log.info("Created user entity");

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userAccount.getUsername(),
                        null,
                        userAccount.getAuthorities()
                );
                log.info("Created authentication");

                authentication.setDetails(new WebAuthenticationDetails(request));
                log.info("Details set");

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Authentication set");
            }

        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
