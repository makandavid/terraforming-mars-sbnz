package com.tm.facts;

import com.tm.enums.CardTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CardInHand extends Card {

    private long playerId;

    // effects on resources
    private int mcIncrease;
    private int energyIncrease;
    private int heatIncrease;
    private int steelIncrease;
    private int titaniumIncrease;
    private int plantIncrease;

    // effects on production
    private int mcProductionIncrease;
    private int energyProductionIncrease;
    private int heatProductionIncrease;
    private int steelProductionIncrease;
    private int titaniumProductionIncrease;
    private int plantProductionIncrease;

    // effects on global parameters
    private int temperatureIncrease;
    private int oxygenIncrease;
    private int oceansPlaced;
    private int terraformRatingIncrease;

    // effects on tiles
    private boolean placesCity;
    private boolean placesGreenery;

    // conditions for playing
    private boolean requiresEnergy;
    private int minTemperature;
    private int maxTemperature;
    private int minOxygen;
    private int maxOxygen;
    private int minOceansPlaced;
    private int maxOceansPlaced;
    private int minGreeneryTiles;
    private int maxGreeneryTiles;
    private int requiredScienceTags;
    private int requiredJovianTags;
    private int requiredPlantTags;
    private int requiredMicrobeTags;
    private int requiredAnimalTags;

    // synergies for relevant rules
    private boolean scienceSynergyBonus;
    private boolean raisesTemperature;

    public CardInHand(long id, String name, List<CardTag> tags, int cost,
                      int vpValue, String description, long playerId) {
        super(id, name, tags, cost, vpValue, description);
        this.playerId = playerId;
    }
}
