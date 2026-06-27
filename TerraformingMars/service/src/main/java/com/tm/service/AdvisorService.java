package com.tm.service;

import com.tm.dto.*;
import com.tm.mapper.GameStateMapper;
import com.tm.output.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.time.SessionPseudoClock;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AdvisorService {

    private final GameStateMapper gameStateMapper;
    private final TemplateRuleService templateRuleService;

    public AdvisorService(GameStateMapper gameStateMapper, TemplateRuleService templateRuleService) {
        this.gameStateMapper = gameStateMapper;
        this.templateRuleService = templateRuleService;
    }

    public AnalysisResponse analyze(GameStateRequest request) {
        KieSession kieSession = templateRuleService.newSessionWithTemplates();

        try {
            SessionPseudoClock clock = kieSession.getSessionClock();
            clock.advanceTime(
                    request.getGeneration() * 1000L,
                    TimeUnit.MILLISECONDS
            );

            gameStateMapper.toFacts(request).forEach(kieSession::insert);

            kieSession.fireAllRules();

            List<RecommendationDto> recommendations = kieSession
                    .getObjects(o -> o instanceof Recommendation)
                    .stream()
                    .map(o -> (Recommendation) o)
                    .sorted(Comparator.comparingInt(r -> r.getPriority().ordinal()))
                    .map(r -> new RecommendationDto(
                            r.getPriority().name(),
                            r.getType().name(),
                            r.getSubject(),
                            r.getExplanation()
                    ))
                    .toList();

            List<StrategicAdviceDto> strategicAdvices = kieSession
                    .getObjects(o -> o instanceof StrategicAdvice)
                    .stream()
                    .map(o -> (StrategicAdvice) o)
                    .map(r -> new StrategicAdviceDto(
                            r.getAdviceType().name(),
                            r.getExplanation()
                    ))
                    .toList();

            List<AlertDto> alerts = kieSession
                    .getObjects(o -> o instanceof Alert)
                    .stream()
                    .map(o -> (Alert) o)
                    .map(a -> new AlertDto(
                            a.getPriority().name(),
                            a.getType().name(),
                            a.getDescription()
                    ))
                    .toList();

            List<InsightDto> insights = kieSession
                    .getObjects(o -> o instanceof Insight)
                    .stream()
                    .map(o -> (Insight) o)
                    .map(i -> new InsightDto(
                            i.getType().name(),
                            i.getDescription()
                    ))
                    .toList();

            List<ThreatAlertDto> threatAlerts = kieSession
                    .getObjects(o -> o instanceof ThreatAlert)
                    .stream()
                    .map(o -> (ThreatAlert) o)
                    .map(t -> new ThreatAlertDto(
                            t.getType().name(),
                            t.getDescription()
                    ))
                    .toList();

            List<MilestoneReportDto> milestoneReports = kieSession
                    .getObjects(o -> o instanceof MilestoneReport)
                    .stream()
                    .map(o -> (MilestoneReport) o)
                    .map(m -> new MilestoneReportDto(
                            m.getMilestoneType().name(),
                            m.isReachableNow(),
                            m.getDescription()
                    ))
                    .toList();

            return new AnalysisResponse(
                    recommendations, strategicAdvices, alerts, insights, threatAlerts, milestoneReports
            );
        } finally {
            kieSession.dispose();
        }
    }
}
