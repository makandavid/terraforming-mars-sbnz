import { useState } from 'react';
import type { GameStateRequest } from './types/requestTypes';
import type { AnalysisResponse } from './types/responseTypes';
import { analyzeGame } from './api/advisor';
import { defaultPlayer, defaultRequest } from './defaultState';
import { GlobalStateSection } from './components/GlobalStateSection';
import { PlayerSection } from './components/PlayerSection';
import { CardSelector } from './components/CardSelector';
import { TileSection } from './components/TileSection';
import { MilestoneSection } from './components/MilestoneSection';
import { AwardSection } from './components/AwardSection';
import { EventSection } from './components/EventSection';
import './App.css';
import { ResultsPanel } from './components/ResultsPanel';

export default function App() {
  const [state, setState] = useState<GameStateRequest>(defaultRequest());
  const [result, setResult] = useState<AnalysisResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const addOpponent = () => {
    const newId = state.opponents.length + 2;  // 1 is current player, 2,3,4,5 are opponents
    setState({
      ...state,
      opponents: [...state.opponents, defaultPlayer(newId)]
    });
  };

  const removeOpponent = (index: number) => {
    setState({
      ...state,
      opponents: state.opponents.filter((_, i) => i !== index)
    });
  };

  const handleAnalyze = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await analyzeGame(state);
      setResult(response);
    } catch (e: any) {
      setError(e.message ?? 'Unknown error');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setState(defaultRequest());
    setResult(null);
    setError(null);
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>Terraforming Mars — Strategic Advisor</h1>
        <p>Powered by Drools rule engine</p>
      </header>

      <div className="app-body">
        {/* Left panel — input form */}
        <div className="form-panel">

          <GlobalStateSection
            state={state}
            onChange={setState}
          />

          <PlayerSection
            label="Your Resources (Player 1)"
            player={state.currentPlayer}
            onChange={p => setState({ ...state, currentPlayer: p })}
          />

          <div className="opponent-controls">
          <span>Opponents: {state.opponents.length}</span>
          <button
            onClick={addOpponent}
            disabled={state.opponents.length >= 4}  // max 5 players in TM
          >
            + Add Opponent
          </button>
          {state.opponents.length > 1 && (
            <button onClick={() => removeOpponent(state.opponents.length - 1)}>
              - Remove Opponent
            </button>
          )}
        </div>

        {state.opponents.map((opp, i) => (
          <PlayerSection
            key={opp.id}
            label={`Opponent ${i + 1} (Player ${opp.id})`}
            player={opp}
            onChange={updated => setState({
              ...state,
              opponents: state.opponents.map((o, j) => j === i ? updated : o)
            })}
          />
        ))}

          <CardSelector
            currentPlayer={state.currentPlayer}
            opponents={state.opponents}
            generation={state.generation}
            cardsInHand={state.cardsInHand}
            playedCards={state.playedCards}
            onHandChange={cards => setState({ ...state, cardsInHand: cards })}
            onPlayedChange={cards => setState({ ...state, playedCards: cards })}
          />

          <TileSection
            tiles={state.tilesPlaced}
            currentPlayer={state.currentPlayer}
            opponents={state.opponents}
            generation={state.generation}
            onChange={tiles => setState({ ...state, tilesPlaced: tiles })}
          />

          <MilestoneSection
            milestones={state.milestones}
            currentPlayer={state.currentPlayer}
            opponents={state.opponents}
            onChange={m => setState({ ...state, milestones: m })}
          />

          <AwardSection
            awards={state.awards}
            currentPlayer={state.currentPlayer}
            opponents={state.opponents}
            onChange={a => setState({ ...state, awards: a })}
          />

          <EventSection
            cardEvents={state.cardPlayedEvents}
            tileEvents={state.tilePlayedEvents}
            tempEvents={state.temperatureRaisedEvents}
            generation={state.generation}
            currentPlayer={state.currentPlayer}
            opponents={state.opponents}
            onCardEventsChange={e => setState({ ...state, cardPlayedEvents: e })}
            onTileEventsChange={e => setState({ ...state, tilePlayedEvents: e })}
            onTempEventsChange={e => setState({ ...state, temperatureRaisedEvents: e })}
          />

          <div className="button-row main-buttons">
            <button className="btn-analyze" onClick={handleAnalyze} disabled={loading}>
              {loading ? 'Analyzing...' : 'Analyze'}
            </button>
            <button className="btn-reset" onClick={handleReset}>
              Reset
            </button>
          </div>

        </div>

        {/* Right panel — results */}
        <div className="results-column">
          <ResultsPanel result={result} loading={loading} error={error} />
        </div>
      </div>
    </div>
  );
}