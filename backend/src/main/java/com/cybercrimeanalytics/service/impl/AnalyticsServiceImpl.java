package com.cybercrimeanalytics.service.impl;

import com.cybercrimeanalytics.dto.response.DashboardStatsResponse;
import com.cybercrimeanalytics.entity.Complaint;
import com.cybercrimeanalytics.entity.ComplaintStatus;
import com.cybercrimeanalytics.entity.SeverityLevel;
import com.cybercrimeanalytics.repository.ComplaintRepository;
import com.cybercrimeanalytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final ComplaintRepository complaintRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse dashboard() {
        var complaints = complaintRepository.findAll((Specification<Complaint>) (root, query, cb) -> cb.isFalse(root.get("deleted")), Pageable.unpaged()).getContent();
        var byCategory = complaints.stream().collect(Collectors.groupingBy(c -> c.getCategory() == null ? "Uncategorized" : c.getCategory().getName(), Collectors.counting()));
        var byMonth = complaints.stream().collect(Collectors.groupingBy(c -> c.getIncidentDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH), Collectors.counting()));
        var bySeverity = complaints.stream().collect(Collectors.groupingBy(c -> c.getSeverity().name(), Collectors.counting()));
        var hotspots = complaints.stream()
                .collect(Collectors.groupingBy(c -> c.getLocation().getCity() == null ? "Unknown" : c.getLocation().getCity()))
                .entrySet().stream()
                .map(entry -> {
                    var first = entry.getValue().get(0);
                    double risk = entry.getValue().stream().map(Complaint::getPrediction).filter(p -> p != null).mapToDouble(p -> p.getEscalationRisk()).average().orElse(0.35);
                    return new DashboardStatsResponse.HotspotPoint(entry.getKey(), first.getLocation().getLatitude(), first.getLocation().getLongitude(), entry.getValue().size(), risk);
                })
                .sorted(Comparator.comparing(DashboardStatsResponse.HotspotPoint::riskScore).reversed())
                .limit(10)
                .toList();
        return new DashboardStatsResponse(
                complaints.size(),
                complaintRepository.countBySeverityAndDeletedFalse(SeverityLevel.HIGH) + complaintRepository.countBySeverityAndDeletedFalse(SeverityLevel.CRITICAL),
                complaintRepository.countByStatusAndDeletedFalse(ComplaintStatus.SUBMITTED) + complaintRepository.countByStatusAndDeletedFalse(ComplaintStatus.UNDER_REVIEW),
                metric(byCategory),
                metric(byMonth),
                metric(bySeverity),
                hotspots
        );
    }

    private java.util.List<DashboardStatsResponse.MetricPoint> metric(Map<String, Long> source) {
        return source.entrySet().stream()
                .map(e -> new DashboardStatsResponse.MetricPoint(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(DashboardStatsResponse.MetricPoint::value).reversed())
                .toList();
    }
}
