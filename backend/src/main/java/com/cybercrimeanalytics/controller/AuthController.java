package com.cybercrimeanalytics.controller;

import com.cybercrimeanalytics.dto.request.ForgotPasswordRequest;
import com.cybercrimeanalytics.dto.request.LoginRequest;
import com.cybercrimeanalytics.dto.request.RefreshTokenRequest;
import com.cybercrimeanalytics.dto.request.RegisterRequest;
import com.cybercrimeanalytics.dto.response.AuthResponse;
import com.cybercrimeanalytics.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh-token")
    AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/forgot-password")
    Map<String, String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return Map.of("message", authService.forgotPassword(request));
    }
}
