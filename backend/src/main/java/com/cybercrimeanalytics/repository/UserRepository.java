package com.cybercrimeanalytics.repository;

import com.cybercrimeanalytics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAndDeletedFalse(String email);
    boolean existsByEmailIgnoreCase(String email);
}
