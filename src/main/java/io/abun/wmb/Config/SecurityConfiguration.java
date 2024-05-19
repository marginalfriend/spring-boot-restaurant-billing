package io.abun.wmb.Config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
//						.httpBasic(new Customizer<HttpBasicConfigurer<HttpSecurity>>() {
//							@Override
//							public void customize(HttpBasicConfigurer<HttpSecurity> httpSecurityHttpBasicConfigurer) {
//
//							}
//						}); // belum jadi lamda
//						.httpBasic(security -> {
//							security.disable();
//						});
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> {
                            req.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                    .requestMatchers("auth/**").permitAll()
                                    .anyRequest().authenticated();
                        }
                )
                .build();

    }
}
