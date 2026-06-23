package com.tm.service;

import com.tm.entity.CardEntity;
import com.tm.enums.CardTag;
import com.tm.facts.CardInHand;
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

        card.setPlacesCity(entity.isPlacesCity());
        card.setPlacesGreenery(entity.isPlacesGreenery());
        card.setRequiresEnergy(entity.isRequiresEnergy());
        card.setRaisesTemperature(entity.isRaisesTemperature());
        card.setScienceSynergyBonus(entity.isScienceSynergyBonus());
        card.setMcProductionIncrease(entity.getMcProductionIncrease());
        card.setEnergyProductionIncrease(entity.getEnergyProductionIncrease());
        card.setHeatProductionIncrease(entity.getHeatProductionIncrease());
        card.setSteelProductionIncrease(entity.getSteelProductionIncrease());
        card.setTitaniumProductionIncrease(entity.getTitaniumProductionIncrease());
        card.setPlantProductionIncrease(entity.getPlantProductionIncrease());
        card.setTemperatureIncrease(entity.getTemperatureIncrease());
        card.setOxygenIncrease(entity.getOxygenIncrease());
        card.setOceansPlaced(entity.getOceansPlaced());

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
