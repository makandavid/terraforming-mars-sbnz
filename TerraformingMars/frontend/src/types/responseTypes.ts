export interface RecommendationDto {
  priority: string;
  type: string;
  subject: string;
  explanation: string;
}

export interface StrategicAdviceDto {
  adviceType: string;
  explanation: string;
}

export interface AlertDto {
  priority: string;
  type: string;
  description: string;
}

export interface InsightDto {
  type: string;
  description: string;
}

export interface ThreatAlertDto {
  type: string;
  description: string;
}

export interface MilestoneReportDto {
  milestone: string;
  reachableNow: boolean;
  description: string;
}

export interface AnalysisResponse {
  recommendations: RecommendationDto[];
  alerts: AlertDto[];
  insights: InsightDto[];
  threatAlerts: ThreatAlertDto[];
  milestoneReports: MilestoneReportDto[];
}

export interface CardCatalogEntry {
  id: number;
  name: string;
  tags: string;
  cost: number;
}