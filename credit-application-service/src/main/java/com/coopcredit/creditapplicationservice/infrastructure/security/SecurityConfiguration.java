package com.coopcredit.creditapplicationservice.infrastructure.security;

import com.coopcredit.creditapplicationservice.domain.model.Role; // Import the class
import com.coopcredit.creditapplicationservice.infrastructure.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Removed static import

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll() // Observability endpoints
                        .requestMatchers("/affiliates/**").hasAnyRole(Role.ADMIN.name(), Role.ANALISTA.name())
                        .requestMatchers("/credit-applications").hasAnyRole(Role.AFILIADO.name(), Role.ANALISTA.name(), Role.ADMIN.name())
                        .requestMatchers("/credit-applications/{id}/evaluate").hasAnyRole(Role.ANALISTA.name(), Role.ADMIN.name())
                        .requestMatchers("/credit-applications/{id}").hasAnyRole(Role.AFILIADO.name(), Role.ANALISTA.name(), Role.ADMIN.name())
                        .requestMatchers("/credit-applications/affiliate/{affiliateId}").hasAnyRole(Role.AFILIADO.name(), Role.ANALISTA.name(), Role.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
