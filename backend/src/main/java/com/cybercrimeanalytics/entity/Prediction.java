package com.cybercrimeanalytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "predictions")
public class Prediction extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id", nullable = false, unique = true)
    private Complaint complaint;

    @Column(nullable = false, length = 80)
    private String predictedCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SeverityLevel predictedSeverity;

    @Column(nullable = false)
    private Double escalationRisk;

    @Column(nullable = false)
    private Double confidenceScore;

    @Column(length = 120)
    private String hotspotLabel;
}
