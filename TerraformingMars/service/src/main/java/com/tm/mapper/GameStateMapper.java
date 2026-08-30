package com.tm.mapper;

import com.tm.CEP.CardPlayedEvent;
import com.tm.CEP.TempRaisedEvent;
import com.tm.CEP.TilePlayedEvent;
import com.tm.dto.*;
import com.tm.enums.AwardType;
import com.tm.enums.CardTag;
import com.tm.enums.MilestoneType;
import com.tm.enums.TileType;
import com.tm.facts.*;
import com.tm.service.CardService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameStateMapper {

    private final CardService cardService;

    public GameStateMapper(CardService cardService) {
        this.cardService = cardService;
    }

    public List<Object> toFacts(GameStateRequest request) {
        List<Object> facts = new ArrayList<>();

        facts.add(new GameState(
                request.getGeneration(),
                request.getOxygenLevel(),
                request.getTemperature(),
                request.getOceanCount(),
                request.getLastTempRaisedGeneration()
        ));

        facts.add(mapPlayer(request.getCurrentPlayer(), true));

        for (PlayerStateDto dto : request.getOpponents()) {
            facts.add(mapPlayer(dto, false));
        }

        for (PlayedCardDto dto : request.getPlayedCards()) {
            facts.add(cardService.resolvePlayedCard(dto));
        }

        long currentPlayerId = request.getCurrentPlayer().getId();
        for (String cardName : request.getCardsInHand()) {
            facts.add(cardService.resolveCardInHand(cardName, currentPlayerId));
        }

        for (TilePlacedDto dto : request.getTilesPlaced()) {
            facts.add(new TilePlaced(
                    dto.getPlayerId(),
                    TileType.valueOf(dto.getTileType()),
                    dto.getGeneration()
            ));
        }

        for (MilestoneDto dto : request.getMilestones()) {
            facts.add(new Milestone(
                    MilestoneType.valueOf(dto.getType()),
                    dto.isClaimed(),
                    dto.getClaimedBy(),
                    dto.getGeneration()
            ));
        }

        for (AwardDto dto : request.getAwards()) {
            facts.add(new Award(
                    AwardType.valueOf(dto.getType()),
                    dto.isFunded(),
                    dto.getFundedBy(),
                    dto.getFundingCost()
            ));
        }

        if (request.getCardPlayedEvents() != null) {
            for (CardPlayedEventDto dto : request.getCardPlayedEvents()) {
                facts.add(new CardPlayedEvent(
                        dto.getPlayerId(),
                        dto.getGeneration(),
                        CardTag.valueOf(dto.getTag())
                ));
            }
        }

        if (request.getTilePlayedEvents() != null) {
            for (TilePlayedEventDto dto : request.getTilePlayedEvents()) {
                facts.add(new TilePlayedEvent(
                        dto.getPlayerId(),
                        dto.getGeneration(),
                        TileType.valueOf(dto.getTileType())
                ));
            }
        }

        if (request.getTemperatureRaisedEvents() != null) {
            for (TemperatureRaisedEventDto dto : request.getTemperatureRaisedEvents()) {
                facts.add(new TempRaisedEvent(
                        dto.getPlayerId(),
                        dto.getGeneration()
                ));
            }
        }

        return facts;
    }

    private PlayerState mapPlayer(PlayerStateDto dto, boolean isCurrent) {
        return new PlayerState(
            dto.getId(), isCurrent,
            dto.getTerraformRating(),
            dto.getMegacredits(), dto.getMcProduction(),
            dto.getSteel(), dto.getSteelProduction(),
            dto.getTitanium(), dto.getTitaniumProduction(),
            dto.getEnergy(), dto.getEnergyProduction(),
            dto.getHeat(), dto.getHeatProduction(),
            dto.getPlants(), dto.getPlantProduction(),
            dto.getCityCount(), dto.getGreeneryCount(),
            dto.getScienceTagCount(), dto.getBuildingTagCount()
        );
    }
}
