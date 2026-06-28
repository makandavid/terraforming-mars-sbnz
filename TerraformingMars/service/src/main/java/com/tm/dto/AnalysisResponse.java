package com.tm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponse {
    private List<RecommendationDto> recommendations;
    private List<StrategicAdviceDto> strategicAdvices;
    private List<AlertDto> alerts;
    private List<InsightDto> insights;
    private List<ThreatAlertDto> threatAlerts;
    private List<MilestoneReportDto> milestoneReports;
    private ScoreProjectionDto scoreProjection;
}
