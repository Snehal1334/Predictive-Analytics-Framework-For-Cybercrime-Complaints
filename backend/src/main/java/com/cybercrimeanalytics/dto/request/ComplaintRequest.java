package com.cybercrimeanalytics.dto.request;

import com.cybercrimeanalytics.entity.ComplaintStatus;
import com.cybercrimeanalytics.entity.SeverityLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "ComplaintRequest", description = "Payload used to create or update a cybercrime complaint.")
public record ComplaintRequest(
        @Schema(example = "UPI fraud complaint", maxLength = 160)
        @NotBlank(message = "Title is required")
        @Size(max = 160, message = "Title cannot exceed 160 characters")
        String title,

        @Schema(example = "A caller pretended to be bank support and stole money through UPI after asking for OTP details.", minLength = 20, maxLength = 8000)
        @NotBlank(message = "Description is required")
        @Size(min = 20, max = 8000, message = "Description must be between 20 and 8000 characters")
        String description,

        @Schema(description = "Optional category id. If omitted, the ML service can predict the category.")
        UUID categoryId,

        @Schema(example = "2026-05-14")
        @PastOrPresent(message = "Incident date cannot be in the future")
        @NotNull(message = "Incident date is required")
        LocalDate incidentDate,

        @Schema(example = "Connaught Place", maxLength = 180)
        @NotBlank(message = "Address is required")
        @Size(max = 180, message = "Address cannot exceed 180 characters")
        String address,

        @Schema(example = "Delhi", maxLength = 80)
        @Size(max = 80, message = "City cannot exceed 80 characters")
        String city,

        @Schema(example = "Delhi", maxLength = 80)
        @Size(max = 80, message = "State cannot exceed 80 characters")
        String state,

        @Schema(example = "110001", maxLength = 20)
        @Size(max = 20, message = "Postal code cannot exceed 20 characters")
        String postalCode,

        @Schema(example = "28.6315", minimum = "-90", maximum = "90")
        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0", message = "Latitude must be at least -90")
        @DecimalMax(value = "90.0", message = "Latitude must be at most 90")
        Double latitude,

        @Schema(example = "77.2167", minimum = "-180", maximum = "180")
        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "Longitude must be at least -180")
        @DecimalMax(value = "180.0", message = "Longitude must be at most 180")
        Double longitude,

        @Schema(example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH", "CRITICAL"})
        SeverityLevel severity,

        @Schema(example = "SUBMITTED", allowableValues = {"SUBMITTED", "UNDER_REVIEW", "ASSIGNED", "ESCALATED", "RESOLVED", "REJECTED"})
        ComplaintStatus status,

        @Schema(description = "Comma-separated attachment URLs or storage keys.", maxLength = 1000)
        @Size(max = 1000, message = "Attachments cannot exceed 1000 characters")
        String attachments
) {}
