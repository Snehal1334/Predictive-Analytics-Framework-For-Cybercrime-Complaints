package com.cybercrimeanalytics.repository;

import com.cybercrimeanalytics.entity.CrimeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CrimeCategoryRepository extends JpaRepository<CrimeCategory, UUID> {
    Optional<CrimeCategory> findByNameIgnoreCaseAndDeletedFalse(String name);
}
