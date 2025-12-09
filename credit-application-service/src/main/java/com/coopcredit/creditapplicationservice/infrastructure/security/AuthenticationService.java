package com.coopcredit.creditapplicationservice.infrastructure.security;

import com.coopcredit.creditapplicationservice.domain.model.User;
import com.coopcredit.creditapplicationservice.infrastructure.persistence.jpa.repository.UserRepository;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.AuthenticationRequest;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.AuthenticationResponse;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.RegisterRequest;
import com.coopcredit.creditapplicationservice.infrastructure.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(request.getRoles());
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
