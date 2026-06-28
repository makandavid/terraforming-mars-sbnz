import type { AnalysisResponse } from "../types/responseTypes";

interface Props {
  result: AnalysisResponse | null;
  loading: boolean;
  error: string | null;
}

const priorityColor: Record<string, string> = {
  URGENT: '#e74c3c',
  HIGH:   '#e67e22',
  MEDIUM: '#3498db',
  LOW:    '#95a5a6',
};

export function ResultsPanel({ result, loading, error }: Props) {
  if (loading) return <div className="results-panel loading">Analyzing...</div>;
  if (error)   return <div className="results-panel error">{error}</div>;
  if (!result) return <div className="results-panel empty">Run analysis to see recommendations</div>;

  return (
    <div className="results-panel">

      {result.scoreProjection && (
        <section>
          <h3>VP Projection</h3>
          <div className="score-projection">
            <div className="score-total">{result.scoreProjection.projectedScore} VP</div>
            <div className="score-breakdown">
              <span>TR: {result.scoreProjection.trContribution}</span>
              <span>Karte: {result.scoreProjection.cardVpContribution}</span>
              <span>Pločice: {result.scoreProjection.tileVpContribution}</span>
              <span>Milestone-ovi: {result.scoreProjection.milestoneVpContribution}</span>
              <span>Nagrade: {result.scoreProjection.awardVpContribution}</span>
            </div>
          </div>
        </section>
      )}

      {result.recommendations.length > 0 && (
        <section>
          <h3>Recommendations</h3>
          {result.recommendations.map((r, i) => (
            <div key={i} className="result-card"
              style={{ borderLeft: `4px solid ${priorityColor[r.priority] ?? '#ccc'}` }}>
              <div className="result-header">
                <span className="priority-badge"
                  style={{ background: priorityColor[r.priority] ?? '#ccc' }}>
                  {r.priority}
                </span>
                <span className="result-subject">{r.subject}</span>
                <span className="result-type">{r.type.replace(/_/g, ' ')}</span>
              </div>
              <p className="result-explanation">{r.explanation}</p>
            </div>
          ))}
        </section>
      )}

      {result.milestoneReports.length > 0 && (
        <section>
          <h3>Milestone Reachability</h3>
          {result.milestoneReports.map((m, i) => (
            <div key={i} className="result-card"
              style={{ borderLeft: `4px solid ${m.reachableNow ? '#2ecc71' : '#f39c12'}` }}>
              <div className="result-header">
                <span className="priority-badge"
                  style={{ background: m.reachableNow ? '#2ecc71' : '#f39c12' }}>
                  {m.reachableNow ? 'NOW' : 'SOON'}
                </span>
                <span className="result-subject">{m.milestone}</span>
              </div>
              <p className="result-explanation">{m.description}</p>
            </div>
          ))}
        </section>
      )}

      {result.alerts.length > 0 && (
        <section>
          <h3>Alerts</h3>
          {result.alerts.map((a, i) => (
            <div key={i} className="result-card alert-card"
              style={{ borderLeft: `4px solid ${priorityColor[a.priority] ?? '#ccc'}` }}>
              <div className="result-header">
                <span className="priority-badge"
                  style={{ background: priorityColor[a.priority] ?? '#ccc' }}>
                  {a.priority}
                </span>
                <span className="result-subject">{a.type.replace(/_/g, ' ')}</span>
              </div>
              <p className="result-explanation">{a.description}</p>
            </div>
          ))}
        </section>
      )}

      {result.threatAlerts.length > 0 && (
        <section>
          <h3>Threat Alerts</h3>
          {result.threatAlerts.map((t, i) => (
            <div key={i} className="result-card threat-card">
              <div className="result-header">
                <span className="priority-badge" style={{ background: '#8e44ad' }}>THREAT</span>
                <span className="result-subject">{t.type.replace(/_/g, ' ')}</span>
              </div>
              <p className="result-explanation">{t.description}</p>
            </div>
          ))}
        </section>
      )}

      {result.insights.length > 0 && (
        <section>
          <h3>Insights</h3>
          {result.insights.map((ins, i) => (
            <div key={i} className="result-card insight-card">
              <span className="result-subject">{ins.type.replace(/_/g, ' ')}</span>
              <p className="result-explanation">{ins.description}</p>
            </div>
          ))}
        </section>
      )}

    </div>
  );
}