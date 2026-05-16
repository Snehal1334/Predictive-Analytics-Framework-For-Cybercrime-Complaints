package com.cybercrimeanalytics.dto.response;

import java.util.List;

public record DashboardStatsResponse(
        long totalComplaints,
        long highSeverityComplaints,
        long openComplaints,
        List<MetricPoint> complaintsByCategory,
        List<MetricPoint> complaintsByMonth,
        List<MetricPoint> severityTrends,
        List<HotspotPoint> hotspots
) {
    public record MetricPoint(String label, long value) {}
    public record HotspotPoint(String label, double latitude, double longitude, long count, double riskScore) {}
}
