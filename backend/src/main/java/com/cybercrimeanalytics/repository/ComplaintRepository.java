package com.cybercrimeanalytics.repository;

import com.cybercrimeanalytics.entity.Complaint;
import com.cybercrimeanalytics.entity.ComplaintStatus;
import com.cybercrimeanalytics.entity.SeverityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.UUID;

public interface ComplaintRepository extends JpaRepository<Complaint, UUID>, JpaSpecificationExecutor<Complaint> {
    long countByDeletedFalse();
    long countBySeverityAndDeletedFalse(SeverityLevel severity);
    long countByStatusAndDeletedFalse(ComplaintStatus status);
    long countByIncidentDateBetweenAndDeletedFalse(LocalDate start, LocalDate end);
}
