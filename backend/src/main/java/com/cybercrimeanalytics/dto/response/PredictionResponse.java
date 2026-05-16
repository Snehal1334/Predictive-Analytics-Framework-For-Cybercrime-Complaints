package com.cybercrimeanalytics.dto.response;

import com.cybercrimeanalytics.entity.SeverityLevel;

public record PredictionResponse(
        String predictedCategory,
        SeverityLevel predictedSeverity,
        Double escalationRisk,
        Double confidenceScore,
        String hotspotLabel
) {}
