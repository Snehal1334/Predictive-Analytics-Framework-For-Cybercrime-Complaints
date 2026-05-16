package com.cybercrimeanalytics.dto.response;

import com.cybercrimeanalytics.entity.ComplaintStatus;
import com.cybercrimeanalytics.entity.SeverityLevel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "ComplaintResponse", description = "Complaint details returned by the complaint API.")
public record ComplaintResponse(
        UUID id,
        String title,
        String description,
        String category,
        LocalDate incidentDate,
        String address,
        String city,
        String state,
        String postalCode,
        Double latitude,
        Double longitude,
        SeverityLevel severity,
        ComplaintStatus status,
        String attachments,
        UserResponse createdBy,
        PredictionResponse prediction,
        Instant createdAt,
        Instant updatedAt
) {}
