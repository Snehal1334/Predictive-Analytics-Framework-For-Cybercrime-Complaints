package com.cybercrimeanalytics.dto.response;

import java.util.Set;
import java.util.UUID;

public record UserResponse(UUID id, String fullName, String email, String phone, Set<String> roles) {}
