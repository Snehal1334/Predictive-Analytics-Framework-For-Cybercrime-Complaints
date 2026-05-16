package com.cybercrimeanalytics.mapper;

import com.cybercrimeanalytics.dto.response.ComplaintResponse;
import com.cybercrimeanalytics.dto.response.PredictionResponse;
import com.cybercrimeanalytics.entity.Complaint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ComplaintMapper {
    private final UserMapper userMapper;

    public ComplaintResponse toResponse(Complaint complaint) {
        var location = complaint.getLocation();
        var category = complaint.getCategory() == null ? null : complaint.getCategory().getName();
        var prediction = complaint.getPrediction() == null ? null : new PredictionResponse(
                complaint.getPrediction().getPredictedCategory(),
                complaint.getPrediction().getPredictedSeverity(),
                complaint.getPrediction().getEscalationRisk(),
                complaint.getPrediction().getConfidenceScore(),
                complaint.getPrediction().getHotspotLabel()
        );
        return new ComplaintResponse(
                complaint.getId(),
                complaint.getTitle(),
                complaint.getDescription(),
                category,
                complaint.getIncidentDate(),
                location.getAddress(),
                location.getCity(),
                location.getState(),
                location.getPostalCode(),
                location.getLatitude(),
                location.getLongitude(),
                complaint.getSeverity(),
                complaint.getStatus(),
                complaint.getAttachments(),
                userMapper.toResponse(complaint.getCreatedBy()),
                prediction,
                complaint.getCreatedAt(),
                complaint.getUpdatedAt()
        );
    }
}
