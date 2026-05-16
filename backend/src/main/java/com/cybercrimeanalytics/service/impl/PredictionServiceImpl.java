package com.cybercrimeanalytics.service.impl;

import com.cybercrimeanalytics.entity.Complaint;
import com.cybercrimeanalytics.entity.Prediction;
import com.cybercrimeanalytics.entity.SeverityLevel;
import com.cybercrimeanalytics.repository.PredictionRepository;
import com.cybercrimeanalytics.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {
    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);
    private final WebClient mlWebClient;
    private final PredictionRepository predictionRepository;

    @Override
    public Prediction predictAndSave(Complaint complaint) {
        MlPrediction ml = callMl(complaint);
        Prediction prediction = predictionRepository.findByComplaintId(complaint.getId()).orElseGet(Prediction::new);
        prediction.setComplaint(complaint);
        prediction.setPredictedCategory(ml.category());
        prediction.setPredictedSeverity(SeverityLevel.valueOf(ml.severity().toUpperCase()));
        prediction.setEscalationRisk(ml.escalationRisk());
        prediction.setConfidenceScore(ml.confidence());
        prediction.setHotspotLabel(complaint.getLocation().getCity() == null ? "Unknown" : complaint.getLocation().getCity());
        return predictionRepository.save(prediction);
    }

    private MlPrediction callMl(Complaint complaint) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = mlWebClient.post()
                    .uri("/api/v1/predict")
                    .bodyValue(Map.of("text", complaint.getTitle() + ". " + complaint.getDescription(),
                            "latitude", complaint.getLocation().getLatitude(),
                            "longitude", complaint.getLocation().getLongitude()))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return new MlPrediction(
                    String.valueOf(response.getOrDefault("category", "Online Fraud")),
                    String.valueOf(response.getOrDefault("severity", "MEDIUM")),
                    asDouble(response.get("escalation_risk"), 0.45),
                    asDouble(response.get("confidence"), 0.70)
            );
        } catch (Exception ex) {
            log.warn("ML service unavailable, using deterministic fallback: {}", ex.getMessage());
            String text = complaint.getDescription().toLowerCase();
            String category = text.contains("bank") || text.contains("upi") ? "Financial Fraud" : text.contains("social") ? "Social Media Abuse" : "Online Fraud";
            String severity = text.contains("threat") || text.contains("blackmail") ? "HIGH" : "MEDIUM";
            double risk = severity.equals("HIGH") ? 0.78 : 0.42;
            return new MlPrediction(category, severity, risk, 0.55);
        }
    }

    private double asDouble(Object value, double fallback) {
        return value instanceof Number number ? number.doubleValue() : fallback;
    }

    private record MlPrediction(String category, String severity, double escalationRisk, double confidence) {}
}
