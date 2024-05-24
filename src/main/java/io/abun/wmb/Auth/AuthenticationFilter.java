package io.abun.wmb.Auth;

import io.abun.wmb.Auth.dto.JwtClaims;
import io.abun.wmb.Auth.interfaces.JwtService;
import io.abun.wmb.Auth.interfaces.UserAccountService;
import io.abun.wmb.Constants.ConstantVariables;
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
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserAccountService userAccountService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String bearerToken = request.getHeader(ConstantVariables.AUTHENTICATION_HEADER);

            if(bearerToken != null && jwtService.verifyJwtToken(bearerToken)) {
                JwtClaims decodedJwt = jwtService.getClaimsByToken(bearerToken);

                UserAccountEntity userAccount = userAccountService.loadUserById(UUID.fromString(decodedJwt.userAccountId()));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userAccount.getUsername(),
                        null,
                        userAccount.getAuthorities()
                );

                // We can extract valuable information of a request such as IP Address of the client
                authentication.setDetails(new WebAuthenticationDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            log.error("Authentication failed : {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
