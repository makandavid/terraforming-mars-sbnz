import type { TilePlacedDto, PlayerStateDto } from "../types/requestTypes";

interface Props {
  tiles: TilePlacedDto[];
  currentPlayer: PlayerStateDto;
  opponents: PlayerStateDto[];
  generation: number;
  onChange: (tiles: TilePlacedDto[]) => void;
}

const TILE_TYPES = ['CITY', 'GREENERY', 'SPECIAL'];

export function TileSection({ tiles, currentPlayer, opponents, generation, onChange }: Props) {
  const allPlayers = [currentPlayer, ...opponents];

  const addTile = (playerId: number, tileType: string) =>
    onChange([...tiles, { playerId, tileType, generation }]);

  const removeTile = (index: number) =>
    onChange(tiles.filter((_, i) => i !== index));

  const playerLabel = (id: number) =>
    id === currentPlayer.id ? `You (P${id})` : `Opponent (P${id})`;

  return (
    <section>
      <h3>Tiles Placed</h3>

      {/* Quick add buttons per player */}
      {allPlayers.map(player => (
        <div key={player.id} className="subsection">
          <h4>{playerLabel(player.id)}</h4>
          <div className="button-row">
            {TILE_TYPES.map(type => (
              <button key={type} onClick={() => addTile(player.id, type)}>
                + {type}
              </button>
            ))}
          </div>
        </div>
      ))}

      {/* All tiles grouped by player */}
      {allPlayers.map(player => {
        const playerTiles = tiles.filter(t => t.playerId === player.id);
        if (playerTiles.length === 0) return null;
        return (
          <div key={player.id} className="player-cards-group">
            <h5>{playerLabel(player.id)}</h5>
            <div className="tag-list">
              {playerTiles.map((t, i) => (
                <span key={i} className="tag">
                  {t.tileType} (gen {t.generation})
                  <button onClick={() => removeTile(
                    tiles.findIndex(x => x === playerTiles[i])
                  )}>×</button>
                </span>
              ))}
            </div>
          </div>
        );
      })}
    </section>
  );
}