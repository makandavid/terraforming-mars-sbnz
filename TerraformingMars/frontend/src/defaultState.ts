import type { GameStateRequest } from "./types/requestTypes";

export const defaultPlayer = (id: number) => ({
    id,
    currentPlayer: false,
    terraformRating: 20,
    megacredits: 10,
    mcProduction: 5,
    steel: 0, steelProduction: 0,
    titanium: 0, titaniumProduction: 0,
    energy: 0, energyProduction: 1,
    heat: 0, heatProduction: 0,
    plants: 0, plantProduction: 1,
    cityCount: 0,
    greeneryCount: 0,
    scienceTagCount: 0,
    buildingTagCount: 0,
});

export const defaultMilestones = [
  { type: 'TERRAFORMER', claimed: false, claimedBy: 0, generation: 0 },
  { type: 'MAYOR',       claimed: false, claimedBy: 0, generation: 0 },
  { type: 'BUILDER',     claimed: false, claimedBy: 0, generation: 0 },
  { type: 'GARDENER',    claimed: false, claimedBy: 0, generation: 0 },
  { type: 'PLANNER',     claimed: false, claimedBy: 0, generation: 0 },
];

export const defaultAwards = [
  { type: 'SCIENTIST',  funded: false, fundedBy: 0, fundingCost: 8 },
  { type: 'BANKER',     funded: false, fundedBy: 0, fundingCost: 8 },
  { type: 'LANDLORD',   funded: false, fundedBy: 0, fundingCost: 8 },
  { type: 'THERMALIST', funded: false, fundedBy: 0, fundingCost: 8 },
  { type: 'MINER',      funded: false, fundedBy: 0, fundingCost: 8 },
];

export const defaultRequest = (): GameStateRequest => ({
    generation: 1,
    oxygenLevel: 0,
    temperature: -30,
    oceanCount: 0,
    lastTempRaisedGeneration: 0,
    currentPlayer: defaultPlayer(1),
    opponents: [defaultPlayer(2)],
    playedCards: [],
    cardsInHand: [],
    tilesPlaced: [],
    milestones: defaultMilestones,
    awards: defaultAwards,
    cardPlayedEvents: [],
    tilePlayedEvents: [],
    temperatureRaisedEvents: [],
});