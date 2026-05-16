package com.cybercrimeanalytics.config;

import com.cybercrimeanalytics.entity.CrimeCategory;
import com.cybercrimeanalytics.entity.Role;
import com.cybercrimeanalytics.entity.RoleName;
import com.cybercrimeanalytics.entity.User;
import com.cybercrimeanalytics.repository.CrimeCategoryRepository;
import com.cybercrimeanalytics.repository.RoleRepository;
import com.cybercrimeanalytics.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    @Bean
    CommandLineRunner seed(RoleRepository roleRepository, UserRepository userRepository,
                           CrimeCategoryRepository categoryRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            for (RoleName name : RoleName.values()) {
                roleRepository.findByName(name).orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    return roleRepository.save(role);
                });
            }
            createCategory(categoryRepository, "Financial Fraud", "Banking, card, UPI and investment fraud");
            createCategory(categoryRepository, "Identity Theft", "Unauthorized use of identity or credentials");
            createCategory(categoryRepository, "Phishing", "Credential capture through deceptive links or messages");
            createCategory(categoryRepository, "Social Media Abuse", "Harassment, impersonation, blackmail or abuse");
            if (!userRepository.existsByEmailIgnoreCase("admin@cyber.local")) {
                User admin = new User();
                admin.setFullName("System Admin");
                admin.setEmail("admin@cyber.local");
                admin.setPasswordHash(passwordEncoder.encode("Admin@12345"));
                admin.getRoles().add(roleRepository.findByName(RoleName.ADMIN).orElseThrow());
                userRepository.save(admin);
            }
        };
    }

    private void createCategory(CrimeCategoryRepository repository, String name, String description) {
        repository.findByNameIgnoreCaseAndDeletedFalse(name).orElseGet(() -> {
            CrimeCategory category = new CrimeCategory();
            category.setName(name);
            category.setDescription(description);
            return repository.save(category);
        });
    }
}
