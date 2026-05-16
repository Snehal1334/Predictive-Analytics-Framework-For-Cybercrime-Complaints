package com.cybercrimeanalytics.repository;

import com.cybercrimeanalytics.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PredictionRepository extends JpaRepository<Prediction, UUID> {
    Optional<Prediction> findByComplaintId(UUID complaintId);
}
