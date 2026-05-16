package com.cybercrimeanalytics.mapper;

import com.cybercrimeanalytics.dto.response.UserResponse;
import com.cybercrimeanalytics.entity.Role;
import com.cybercrimeanalytics.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRoles().stream().map(Role::getName).map(Enum::name).collect(Collectors.toSet())
        );
    }
}
