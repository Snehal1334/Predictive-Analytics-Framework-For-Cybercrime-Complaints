package com.cybercrimeanalytics.dto.request;

import com.cybercrimeanalytics.entity.ComplaintStatus;
import com.cybercrimeanalytics.entity.SeverityLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "ComplaintSearchRequest", description = "Query parameters used for complaint searching, filtering, pagination and sorting.")
public record ComplaintSearchRequest(
        @Schema(description = "Searches title and description.", example = "upi fraud")
        String query,

        @Schema(description = "Filter by crime category id.")
        UUID categoryId,

        @Schema(example = "HIGH")
        SeverityLevel severity,

        @Schema(example = "UNDER_REVIEW")
        ComplaintStatus status,

        @Schema(description = "Filter complaints from this incident date.", example = "2026-01-01")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,

        @Schema(description = "Filter complaints until this incident date.", example = "2026-05-14")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
) {}
