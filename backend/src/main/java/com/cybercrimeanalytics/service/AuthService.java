package com.cybercrimeanalytics.service;

import com.cybercrimeanalytics.dto.request.ForgotPasswordRequest;
import com.cybercrimeanalytics.dto.request.LoginRequest;
import com.cybercrimeanalytics.dto.request.RefreshTokenRequest;
import com.cybercrimeanalytics.dto.request.RegisterRequest;
import com.cybercrimeanalytics.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RefreshTokenRequest request);
    String forgotPassword(ForgotPasswordRequest request);
}
