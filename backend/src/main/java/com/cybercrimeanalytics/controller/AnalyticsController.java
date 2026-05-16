package com.cybercrimeanalytics.controller;

import com.cybercrimeanalytics.dto.response.DashboardStatsResponse;
import com.cybercrimeanalytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    DashboardStatsResponse dashboard() {
        return analyticsService.dashboard();
    }
}
