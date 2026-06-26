import type { AwardDto, PlayerStateDto } from "../types/requestTypes";

interface Props {
  awards: AwardDto[];
  currentPlayer: PlayerStateDto;
  opponents: PlayerStateDto[];
  onChange: (updated: AwardDto[]) => void;
}

const FUNDING_COSTS = [8, 14, 20];

export function AwardSection({ awards, currentPlayer, opponents, onChange }: Props) {
  const allPlayers = [currentPlayer, ...opponents];
  const fundedCount = awards.filter(a => a.funded).length;
  const MAX_FUNDED = 3;

  const toggle = (index: number) => {
    const current = awards[index];
    if (!current.funded && fundedCount >= MAX_FUNDED) return;
    const newFunded = !current.funded;
    const cost = newFunded ? (FUNDING_COSTS[fundedCount] ?? 20) : 0;
    onChange(awards.map((a, i) =>
      i === index
        ? { ...a, funded: newFunded, fundedBy: newFunded ? currentPlayer.id : 0, fundingCost: cost }
        : a
    ));
  };

  const setFundedBy = (index: number, playerId: number) => {
    onChange(awards.map((a, i) =>
      i === index ? { ...a, fundedBy: playerId } : a
    ));
  };

  const playerLabel = (p: PlayerStateDto, i: number) =>
    i === 0 ? `Player ${p.id} (You)` : `Player ${p.id} (Opponent ${i})`;

  return (
    <section>
      <h3>Awards</h3>

      <div className="section-counter">
        <span className={fundedCount >= MAX_FUNDED ? 'counter-full' : 'counter-ok'}>
          {fundedCount} / {MAX_FUNDED} funded
        </span>
        {fundedCount >= MAX_FUNDED && (
          <span className="counter-warning">
            Maximum reached — no more awards can be funded
          </span>
        )}
      </div>

      <table>
        <thead>
          <tr>
            <th>Award</th>
            <th>Wins By</th>
            <th>Funded</th>
            <th>Funded By</th>
            <th>Cost</th>
          </tr>
        </thead>
        <tbody>
          {awards.map((a, i) => {
            const wouldExceedMax = !a.funded && fundedCount >= MAX_FUNDED;
            return (
              <tr key={a.type} className={wouldExceedMax ? 'row-disabled' : ''}>
                <td>{a.type}</td>
                <td style={{ color: '#888', fontSize: '0.75rem' }}>
                  {a.type === 'SCIENTIST'  && 'Most science tags'}
                  {a.type === 'BANKER'     && 'Highest MC production'}
                  {a.type === 'LANDLORD'   && 'Most tiles on board'}
                  {a.type === 'THERMALIST' && 'Most heat resources'}
                  {a.type === 'MINER'      && 'Most steel + titanium'}
                </td>
                <td>
                  <input
                    type="checkbox"
                    checked={a.funded}
                    disabled={wouldExceedMax}
                    onChange={() => toggle(i)}
                    title={wouldExceedMax ? 'Max 3 awards can be funded' : ''}
                  />
                </td>
                <td>
                  {a.funded && (
                    <select
                      value={a.fundedBy}
                      onChange={e => setFundedBy(i, +e.target.value)}
                    >
                      {allPlayers.map((p, pi) => (
                        <option key={p.id} value={p.id}>
                          {playerLabel(p, pi)}
                        </option>
                      ))}
                    </select>
                  )}
                </td>
                <td>
                  {a.funded
                    ? <span style={{ color: '#f39c12' }}>{a.fundingCost} MC</span>
                    : '—'
                  }
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>

      <p style={{ fontSize: '0.75rem', color: '#888', marginTop: '8px' }}>
        1st award = 8 MC · 2nd = 14 MC · 3rd = 20 MC
      </p>
    </section>
  );
}