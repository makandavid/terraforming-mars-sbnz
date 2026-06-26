export interface PlayerStateDto {
    id: number;
    currentPlayer: boolean;
    terraformRating: number;
    megacredits: number;
    mcProduction: number;
    steel: number;
    steelProduction: number;
    titanium: number;
    titaniumProduction: number;
    energy: number;
    energyProduction: number;
    heat: number;
    heatProduction: number;
    plants: number;
    plantProduction: number;
    cityCount: number;
    greeneryCount: number;
    scienceTagCount: number;
    buildingTagCount: number;
}

export interface PlayedCardDto {
  name: string;
  tags: string[];
  playerId: number;
  generationPlayed: number;
}

export interface TilePlacedDto {
  playerId: number;
  tileType: string;
  generation: number;
}

export interface MilestoneDto {
  type: string;
  claimed: boolean;
  claimedBy: number;
  generation: number;
}

export interface AwardDto {
  type: string;
  funded: boolean;
  fundedBy: number;
  fundingCost: number;
}

export interface CardPlayedEventDto {
  playerId: number;
  tag: string;
  generation: number;
}

export interface TilePlayedEventDto {
  playerId: number;
  tileType: string;
  generation: number;
}

export interface TemperatureRaisedEventDto {
  playerId: number;
  generation: number;
}

export interface GameStateRequest {
  generation: number;
  oxygenLevel: number;
  temperature: number;
  oceanCount: number;
  lastTempRaisedGeneration: number;
  currentPlayer: PlayerStateDto;
  opponents: PlayerStateDto[];
  playedCards: PlayedCardDto[];
  cardsInHand: string[];
  tilesPlaced: TilePlacedDto[];
  milestones: MilestoneDto[];
  awards: AwardDto[];
  cardPlayedEvents: CardPlayedEventDto[];
  tilePlayedEvents: TilePlayedEventDto[];
  temperatureRaisedEvents: TemperatureRaisedEventDto[];
}