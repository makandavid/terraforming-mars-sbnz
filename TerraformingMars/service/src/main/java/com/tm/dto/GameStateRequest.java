package com.tm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameStateRequest {
    private int generation;
    private double oxygenLevel;
    private double temperature;
    private int oceanCount;
    private int lastTempRaisedGeneration;

    private PlayerStateDto currentPlayer;
    private List<PlayerStateDto> opponents;

    private List<PlayedCardDto> playedCards;
    private List<String> cardsInHand;
    private List<TilePlacedDto> tilesPlaced;
    private List<MilestoneDto> milestones;
    private List<AwardDto> awards;

    private List<CardPlayedEventDto> cardPlayedEvents;
    private List<TilePlayedEventDto> tilePlayedEvents;
    private List<TemperatureRaisedEventDto> temperatureRaisedEvents;
}
