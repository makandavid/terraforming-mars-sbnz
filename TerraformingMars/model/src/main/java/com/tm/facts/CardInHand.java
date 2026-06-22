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

    // effects on tiles
    private boolean placesCity;
    private boolean placesGreenery;

    // conditions for playing
    private boolean requiresEnergy;
    private int minTemperature;
    private int minOxygen;
    private int requiredScienceTags;

    // synergies for relevant rules
    private boolean scienceSynergyBonus;
    private boolean raisesTemperature;

    public CardInHand(long id, String name, List<CardTag> tags, int cost,
                      int vpValue, String description, long playerId) {
        super(id, name, tags, cost, vpValue, description);
        this.playerId = playerId;
    }
}
