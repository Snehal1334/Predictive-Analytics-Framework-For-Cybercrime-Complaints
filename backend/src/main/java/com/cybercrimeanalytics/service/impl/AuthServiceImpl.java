package com.cybercrimeanalytics.service.impl;

import com.cybercrimeanalytics.dto.request.ForgotPasswordRequest;
import com.cybercrimeanalytics.dto.request.LoginRequest;
import com.cybercrimeanalytics.dto.request.RefreshTokenRequest;
import com.cybercrimeanalytics.dto.request.RegisterRequest;
import com.cybercrimeanalytics.dto.response.AuthResponse;
import com.cybercrimeanalytics.entity.RoleName;
import com.cybercrimeanalytics.entity.User;
import com.cybercrimeanalytics.exception.ApiException;
import com.cybercrimeanalytics.mapper.UserMapper;
import com.cybercrimeanalytics.repository.RoleRepository;
import com.cybercrimeanalytics.repository.UserRepository;
import com.cybercrimeanalytics.security.CustomUserDetailsService;
import com.cybercrimeanalytics.security.jwt.JwtService;
import com.cybercrimeanalytics.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email is already registered");
        }
        var role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role missing"));
        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setPhone(request.phone());
        user.getRoles().add(role);
        userRepository.save(user);
        var details = userDetailsService.loadUserByUsername(user.getEmail());
        return tokens(details, user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password()));
        var user = userRepository.findByEmailAndDeletedFalse(request.email().toLowerCase())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        var details = userDetailsService.loadUserByUsername(user.getEmail());
        return tokens(details, user);
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        if (!jwtService.isRefreshToken(request.refreshToken())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
        var details = userDetailsService.loadUserByUsername(jwtService.subject(request.refreshToken()));
        if (!jwtService.isValid(request.refreshToken(), details)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Expired refresh token");
        }
        var user = userRepository.findByEmailAndDeletedFalse(details.getUsername())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));
        return tokens(details, user);
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        return "If the account exists, a password reset link will be sent. Mock reset token: RESET-" + Math.abs(request.email().hashCode());
    }

    private AuthResponse tokens(org.springframework.security.core.userdetails.UserDetails details, User user) {
        return new AuthResponse(jwtService.accessToken(details), jwtService.refreshToken(details), "Bearer", userMapper.toResponse(user));
    }
}
