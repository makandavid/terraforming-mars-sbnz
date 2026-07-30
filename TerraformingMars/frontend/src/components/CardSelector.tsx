import { useState, useEffect } from 'react';
import { fetchCards } from '../api/advisor';
import type { PlayerStateDto, PlayedCardDto } from '../types/requestTypes';
import type { CardCatalogEntry } from '../types/responseTypes';

interface Props {
  currentPlayer: PlayerStateDto;
  opponents: PlayerStateDto[];
  generation: number;
  cardsInHand: string[];
  playedCards: PlayedCardDto[];
  onHandChange: (cards: string[]) => void;
  onPlayedChange: (cards: PlayedCardDto[]) => void;
}

const CARD_TAGS = ['SCIENCE', 'BUILDING', 'POWER', 'EARTH', 'JOVIAN',
                   'CITY', 'ANIMAL', 'MICROBE', 'PLANT', 'SPACE', 'EVENT'];

export function CardSelector({
  currentPlayer, opponents, generation,
  cardsInHand, playedCards,
  onHandChange, onPlayedChange
}: Props) {
  const [catalog, setCatalog] = useState<CardCatalogEntry[]>([]);
  const [search, setSearch] = useState('');

  // State for adding a played card
  const [playedName, setPlayedName] = useState('');
  const [playedPlayerSearch, setPlayedPlayerSearch] = useState('');
  const [playedTags, setPlayedTags] = useState<string[]>([]);
  const [playedPlayerId, setPlayedPlayerId] = useState<number>(currentPlayer.id);
  const [playedGen, setPlayedGen] = useState<number>(generation);

  const allPlayers = [currentPlayer, ...opponents];

  useEffect(() => {
    fetchCards().then(setCatalog).catch(console.error);
  }, []);

  // Cards in hand autocomplete — only for current player
  const handSuggestions = catalog
    .filter(c =>
      c.name.toLowerCase().includes(search.toLowerCase()) &&
      !cardsInHand.includes(c.name)
    )
    .slice(0, 8);

  const addToHand = (name: string) => {
    onHandChange([...cardsInHand, name]);
    setSearch('');
  };

  const removeFromHand = (name: string) =>
    onHandChange(cardsInHand.filter(c => c !== name));

  // Played card search autocomplete
  const playedSuggestions = catalog
    .filter(c => c.name.toLowerCase().includes(playedPlayerSearch.toLowerCase()))
    .slice(0, 8);

  const toggleTag = (tag: string) =>
    setPlayedTags(prev =>
      prev.includes(tag) ? prev.filter(t => t !== tag) : [...prev, tag]
    );

  const addPlayedCard = () => {
    if (!playedName.trim()) return;
    const card: PlayedCardDto = {
      name: playedName.trim(),
      tags: playedTags,
      playerId: playedPlayerId,
      generationPlayed: playedGen,
    };
    onPlayedChange([...playedCards, card]);
    setPlayedName('');
    setPlayedPlayerSearch('');
    setPlayedTags([]);
  };

  const removePlayedCard = (index: number) =>
    onPlayedChange(playedCards.filter((_, i) => i !== index));

  const playerLabel = (id: number) => {
    if (id === currentPlayer.id) return `Player ${id} (You)`;
    return `Player ${id} (Opponent ${opponents.findIndex(o => o.id === id) + 1})`;
  };

  return (
    <section>
      <h3>Cards</h3>

      {/* Cards in hand — only current player */}
      <div className="subsection">
        <h4>Your Cards in Hand</h4>
        <div className="search-box">
          <input
            type="text"
            placeholder="Search card name from catalog..."
            value={search}
            onChange={e => setSearch(e.target.value)}
          />
          {search && handSuggestions.length > 0 && (
            <ul className="suggestions">
              {handSuggestions.map(c => (
                <li key={c.id} onClick={() => addToHand(c.name)}>
                  <span>{c.name}</span>
                  <span className="tags">{c.tags}</span>
                  <span className="cost">{c.cost} MC</span>
                </li>
              ))}
            </ul>
          )}
        </div>
        <div className="tag-list">
          {cardsInHand.map(name => (
            <span key={name} className="tag">
              {name}
              <button onClick={() => removeFromHand(name)}>×</button>
            </span>
          ))}
        </div>
      </div>

      {/* Played cards — any player */}
      <div className="subsection">
        <h4>Add Played Card</h4>
        <div className="played-card-form">

          {/* Who played it */}
          <label>
            Player
            <select
              value={playedPlayerId}
              onChange={e => setPlayedPlayerId(+e.target.value)}
            >
              {allPlayers.map(p => (
                <option key={p.id} value={p.id}>{playerLabel(p.id)}</option>
              ))}
            </select>
          </label>

          {/* Card name with autocomplete */}
          <label>
            Card Name
            <div className="search-box">
              <input
                type="text"
                placeholder="Type card name or enter manually..."
                value={playedPlayerSearch || playedName}
                onChange={e => {
                  setPlayedPlayerSearch(e.target.value);
                  setPlayedName(e.target.value);
                }}
              />
              {playedPlayerSearch && playedSuggestions.length > 0 && (
                <ul className="suggestions">
                  {playedSuggestions.map(c => (
                    <li key={c.id} onClick={() => {
                      setPlayedName(c.name);
                      setPlayedPlayerSearch('');
                      // Auto-fill tags from catalog
                      setPlayedTags(c.tags.split(',').map(t => t.trim()));
                    }}>
                      <span>{c.name}</span>
                      <span className="tags">{c.tags}</span>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          </label>

          {/* Generation played */}
          <label>
            Generation Played
            <input
              type="number"
              min={1}
              max={14}
              value={playedGen}
              onChange={e => setPlayedGen(+e.target.value)}
            />
          </label>

          {/* Tags */}
          <div className="tag-picker">
            <span className="tag-picker-label">Tags:</span>
            {CARD_TAGS.map(tag => (
              <button
                key={tag}
                className={`tag-toggle ${playedTags.includes(tag) ? 'selected' : ''}`}
                onClick={() => toggleTag(tag)}
              >
                {tag}
              </button>
            ))}
          </div>
          {playedTags.length > 0 && (
            <div className="selected-tags">
              Selected: {playedTags.join(', ')}
            </div>
          )}

          <button className="btn-add-card" onClick={addPlayedCard}>
            Add Played Card
          </button>
        </div>

        {/* List of played cards grouped by player */}
        <div className="played-cards-list">
          {allPlayers.map(player => {
            const playerCards = playedCards.filter(c => c.playerId === player.id);
            if (playerCards.length === 0) return null;
            return (
              <div key={player.id} className="player-cards-group">
                <h5>{playerLabel(player.id)}</h5>
                <div className="tag-list">
                  {playerCards.map((c, i) => (
                    <span key={i} className="tag played">
                      {c.name}
                      {c.tags.length > 0 && (
                        <span className="tags"> [{c.tags.join(',')}]</span>
                      )}
                      <span className="gen"> gen {c.generationPlayed}</span>
                      <button onClick={() => removePlayedCard(
                        playedCards.findIndex((pc, _) =>
                          pc.playerId === player.id &&
                          playedCards.filter(x => x.playerId === player.id)[i] === pc
                        )
                      )}>×</button>
                    </span>
                  ))}
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
}