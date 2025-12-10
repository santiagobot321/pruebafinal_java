package com.coopcredit.creditapplicationservice.infrastructure.rest.controller;

import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.AuthenticationRequest;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.AuthenticationResponse;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.RegisterRequest;
import com.coopcredit.creditapplicationservice.infrastructure.security.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
