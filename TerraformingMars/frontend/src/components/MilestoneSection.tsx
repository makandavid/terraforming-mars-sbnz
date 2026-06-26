import type { MilestoneDto, PlayerStateDto } from "../types/requestTypes";

interface Props {
  milestones: MilestoneDto[];
  currentPlayer: PlayerStateDto;
  opponents: PlayerStateDto[];
  onChange: (updated: MilestoneDto[]) => void;
}

export function MilestoneSection({ milestones, currentPlayer, opponents, onChange }: Props) {
  const allPlayers = [currentPlayer, ...opponents];
  const claimedCount = milestones.filter(m => m.claimed).length;
  const MAX_CLAIMED = 3;

  const toggle = (index: number) => {
    const current = milestones[index];
    if (!current.claimed && claimedCount >= MAX_CLAIMED) return;
    const updated = milestones.map((m, i) =>
      i === index
        ? { ...m, claimed: !current.claimed, claimedBy: !current.claimed ? currentPlayer.id : 0 }
        : m
    );
    onChange(updated);
  };

  const setClaimedBy = (index: number, playerId: number) => {
    onChange(milestones.map((m, i) =>
      i === index ? { ...m, claimedBy: playerId } : m
    ));
  };

  const playerLabel = (p: PlayerStateDto, i: number) =>
    i === 0 ? `Player ${p.id} (You)` : `Player ${p.id} (Opponent ${i})`;

  return (
    <section>
      <h3>Milestones</h3>

      <div className="section-counter">
        <span className={claimedCount >= MAX_CLAIMED ? 'counter-full' : 'counter-ok'}>
          {claimedCount} / {MAX_CLAIMED} claimed
        </span>
        {claimedCount >= MAX_CLAIMED && (
          <span className="counter-warning">
            Maximum reached — no more milestones can be claimed
          </span>
        )}
      </div>

      <table>
        <thead>
          <tr>
            <th>Milestone</th>
            <th>Condition</th>
            <th>Claimed</th>
            <th>Claimed By</th>
          </tr>
        </thead>
        <tbody>
          {milestones.map((m, i) => {
            const wouldExceedMax = !m.claimed && claimedCount >= MAX_CLAIMED;
            return (
              <tr key={m.type} className={wouldExceedMax ? 'row-disabled' : ''}>
                <td>{m.type}</td>
                <td style={{ color: '#888', fontSize: '0.75rem' }}>
                  {m.type === 'TERRAFORMER' && 'TR ≥ 35'}
                  {m.type === 'MAYOR'       && '3 cities'}
                  {m.type === 'BUILDER'     && '8 building tags'}
                  {m.type === 'GARDENER'    && '3 greeneries'}
                  {m.type === 'PLANNER'     && '16 cards in hand'}
                </td>
                <td>
                  <input
                    type="checkbox"
                    checked={m.claimed}
                    disabled={wouldExceedMax}
                    onChange={() => toggle(i)}
                    title={wouldExceedMax ? 'Max 3 milestones can be claimed' : ''}
                  />
                </td>
                <td>
                  {m.claimed && (
                    <select
                      value={m.claimedBy}
                      onChange={e => setClaimedBy(i, +e.target.value)}
                    >
                      {allPlayers.map((p, pi) => (
                        <option key={p.id} value={p.id}>
                          {playerLabel(p, pi)}
                        </option>
                      ))}
                    </select>
                  )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </section>
  );
}