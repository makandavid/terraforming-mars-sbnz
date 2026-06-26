import { useState } from 'react';
import type { CardPlayedEventDto, TilePlayedEventDto, TemperatureRaisedEventDto, PlayerStateDto } from '../types/requestTypes';

interface Props {
  cardEvents: CardPlayedEventDto[];
  tileEvents: TilePlayedEventDto[];
  tempEvents: TemperatureRaisedEventDto[];
  generation: number;
  currentPlayer: PlayerStateDto;
  opponents: PlayerStateDto[];
  onCardEventsChange: (e: CardPlayedEventDto[]) => void;
  onTileEventsChange: (e: TilePlayedEventDto[]) => void;
  onTempEventsChange: (e: TemperatureRaisedEventDto[]) => void;
}

const CARD_TAGS = [
  'SCIENCE', 'BUILDING', 'POWER', 'EARTH',
  'JOVIAN', 'CITY', 'ANIMAL', 'MICROBE', 'PLANT', 'SPACE'
];

export function EventSection({
  cardEvents, tileEvents, tempEvents, generation,
  currentPlayer, opponents,
  onCardEventsChange, onTileEventsChange, onTempEventsChange
}: Props) {

  // State for CEP-1 — science card played by opponent
  const [cardTag, setCardTag] = useState('SCIENCE');
  const [cardPlayerId, setCardPlayerId] = useState<number>(
    opponents.length > 0 ? opponents[0].id : 2
  );
  const [cardGen, setCardGen] = useState(generation);

  // State for CEP-2 — city tile placed by opponent
  const [cityPlayerId, setCityPlayerId] = useState<number>(
    opponents.length > 0 ? opponents[0].id : 2
  );
  const [cityGen, setCityGen] = useState(generation);

  // State for CEP-3 — temperature raised by current player
  const [tempGen, setTempGen] = useState(generation);

  const addCardEvent = () => {
    onCardEventsChange([
      ...cardEvents,
      { playerId: cardPlayerId, tag: cardTag, generation: cardGen }
    ]);
  };

  const addCityEvent = () => {
    onTileEventsChange([
      ...tileEvents,
      { playerId: cityPlayerId, tileType: 'CITY', generation: cityGen }
    ]);
  };

  const addTempEvent = () => {
    onTempEventsChange([
      ...tempEvents,
      { playerId: currentPlayer.id, generation: tempGen }
    ]);
  };

  const playerLabel = (id: number) => {
    if (id === currentPlayer.id) return `Player ${id} (You)`;
    const idx = opponents.findIndex(o => o.id === id);
    return `Player ${id} (Opponent ${idx + 1})`;
  };

  return (
    <section>
      <h3>CEP Events</h3>
      <p className="section-hint">
        Record what happened in previous generations so the system
        can detect time-based patterns.
      </p>

      {/* ── CEP-1: Science card played ─────────────────────── */}
      <div className="subsection">
        <h4>CEP-1 — Science card played by opponent</h4>
        <p className="rule-hint">
          Detects if 3+ science cards were played in 2 generations (science rush).
        </p>
        <div className="event-form">
          <label>
            Opponent
            <select
              value={cardPlayerId}
              onChange={e => setCardPlayerId(+e.target.value)}
            >
              {opponents.map(o => (
                <option key={o.id} value={o.id}>{playerLabel(o.id)}</option>
              ))}
            </select>
          </label>
          <label>
            Tag
            <select value={cardTag} onChange={e => setCardTag(e.target.value)}>
              {CARD_TAGS.map(t => (
                <option key={t} value={t}>{t}</option>
              ))}
            </select>
          </label>
          <label>
            Generation
            <input
              type="number"
              min={1}
              max={14}
              value={cardGen}
              onChange={e => setCardGen(+e.target.value)}
            />
          </label>
          <button onClick={addCardEvent}>Add Card Event</button>
        </div>
        <div className="tag-list">
          {cardEvents.map((e, i) => (
            <span key={i} className="tag event-tag">
              {playerLabel(e.playerId)}: played {e.tag} card (gen {e.generation})
              <button onClick={() =>
                onCardEventsChange(cardEvents.filter((_, j) => j !== i))
              }>×</button>
            </span>
          ))}
        </div>
      </div>

      {/* ── CEP-2: City tile placed ────────────────────────── */}
      <div className="subsection">
        <h4>CEP-2 — City tile placed by opponent</h4>
        <p className="rule-hint">
          Detects if opponent placed 2 cities within 2 generations (Mayor rush).
        </p>
        <div className="event-form">
          <label>
            Opponent
            <select
              value={cityPlayerId}
              onChange={e => setCityPlayerId(+e.target.value)}
            >
              {opponents.map(o => (
                <option key={o.id} value={o.id}>{playerLabel(o.id)}</option>
              ))}
            </select>
          </label>
          <label>
            Generation
            <input
              type="number"
              min={1}
              max={14}
              value={cityGen}
              onChange={e => setCityGen(+e.target.value)}
            />
          </label>
          <button onClick={addCityEvent}>Add City Event</button>
        </div>
        <div className="tag-list">
          {tileEvents.map((e, i) => (
            <span key={i} className="tag event-tag">
              {playerLabel(e.playerId)}: placed {e.tileType} (gen {e.generation})
              <button onClick={() =>
                onTileEventsChange(tileEvents.filter((_, j) => j !== i))
              }>×</button>
            </span>
          ))}
        </div>
      </div>

      {/* ── CEP-3: Temperature raised ──────────────────────── */}
      <div className="subsection">
        <h4>CEP-3 — Temperature raised by you</h4>
        <p className="rule-hint">
          If you haven't raised temperature in 2+ generations while holding
          heat cards, the system will warn you. Record when you last raised it.
        </p>
        <div className="event-form">
          <label>
            Generation raised
            <input
              type="number"
              min={1}
              max={14}
              value={tempGen}
              onChange={e => setTempGen(+e.target.value)}
            />
          </label>
          <button onClick={addTempEvent}>Add Temperature Event</button>
        </div>
        <div className="tag-list">
          {tempEvents.map((e, i) => (
            <span key={i} className="tag event-tag">
              You raised temperature in gen {e.generation}
              <button onClick={() =>
                onTempEventsChange(tempEvents.filter((_, j) => j !== i))
              }>×</button>
            </span>
          ))}
        </div>
      </div>

    </section>
  );
}