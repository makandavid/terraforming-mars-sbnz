package com.tm.service;

import com.tm.dto.PlayedCardDto;
import com.tm.entity.CardEntity;
import com.tm.enums.CardTag;
import com.tm.facts.CardInHand;
import com.tm.facts.PlayedCard;
import com.tm.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public PlayedCard resolvePlayedCard(PlayedCardDto playedCardDto) {
        CardEntity entity = cardRepository.findByName(playedCardDto.getName())
                .orElseThrow(() -> new RuntimeException(
                        "Card not found in catalog: " + playedCardDto.getName()
                ));

        List<CardTag> tags = playedCardDto.getTags().stream()
                .map(CardTag::valueOf)
                .toList();

        return new PlayedCard(
                System.nanoTime(),
                playedCardDto.getName(),
                tags,
                entity.getCost(),
                entity.getVpValue(),
                "",
                playedCardDto.getPlayerId(),
                playedCardDto.getGenerationPlayed()
        );
    }

    public CardInHand resolveCardInHand(String cardName, long playerId) {
        CardEntity entity = cardRepository.findByName(cardName)
                .orElseThrow(() -> new RuntimeException(
                        "Card not found in catalog: " + cardName
                ));

        List<CardTag> tags = parseTags(entity.getTags());

        CardInHand card = new CardInHand(
                entity.getId(),
                entity.getName(),
                tags,
                entity.getCost(),
                entity.getVpValue(),
                "",
                playerId
        );

        // --- Flat Resource Increases ---
        card.setMcIncrease(entity.getMcIncrease());
        card.setEnergyIncrease(entity.getEnergyIncrease());
        card.setHeatIncrease(entity.getHeatIncrease());
        card.setSteelIncrease(entity.getSteelIncrease());
        card.setTitaniumIncrease(entity.getTitaniumIncrease());
        card.setPlantIncrease(entity.getPlantIncrease());

        // --- Production Increases ---
        card.setMcProductionIncrease(entity.getMcProductionIncrease());
        card.setEnergyProductionIncrease(entity.getEnergyProductionIncrease());
        card.setHeatProductionIncrease(entity.getHeatProductionIncrease());
        card.setSteelProductionIncrease(entity.getSteelProductionIncrease());
        card.setTitaniumProductionIncrease(entity.getTitaniumProductionIncrease());
        card.setPlantProductionIncrease(entity.getPlantProductionIncrease());

        // --- Global Parameter Effects ---
        card.setTemperatureIncrease(entity.getTemperatureIncrease());
        card.setOxygenIncrease(entity.getOxygenIncrease());
        card.setOceansPlaced(entity.getOceansPlaced());
        card.setTerraformRatingIncrease(entity.getTerraformRatingIncrease());

        // --- Map Actions & Bonuses ---
        card.setPlacesCity(entity.isPlacesCity());
        card.setPlacesGreenery(entity.isPlacesGreenery());
        card.setRaisesTemperature(entity.isRaisesTemperature());
        card.setScienceSynergyBonus(entity.isScienceSynergyBonus());

        // --- Requirements ---
        card.setRequiresEnergy(entity.isRequiresEnergy());
        card.setMinTemperature(entity.getMinTemperature());
        card.setMaxTemperature(entity.getMaxTemperature());
        card.setMinOxygen(entity.getMinOxygen());
        card.setMaxOxygen(entity.getMaxOxygen());
        card.setMinOceansPlaced(entity.getMinOceansPlaced());
        card.setMaxOceansPlaced(entity.getMaxOceansPlaced());
        card.setMinGreeneryTiles(entity.getMinGreeneryTiles());
        card.setMaxGreeneryTiles(entity.getMaxGreeneryTiles());
        card.setRequiredScienceTags(entity.getRequiredScienceTags());
        card.setRequiredJovianTags(entity.getRequiredJovianTags());
        card.setRequiredPlantTags(entity.getRequiredPlantTags());
        card.setRequiredMicrobeTags(entity.getRequiredMicrobeTags());
        card.setRequiredAnimalTags(entity.getRequiredAnimalTags());

        return card;
    }

    public List<CardEntity> getAllCards() {
        return cardRepository.findAll();
    }

    private List<CardTag> parseTags(String tagsString) {
        if (tagsString == null || tagsString.isBlank()) {
            return List.of();
        }
        return Arrays.stream(tagsString.split(","))
                .map(String::trim)
                .map(CardTag::valueOf)
                .collect(Collectors.toList());
    }
}
