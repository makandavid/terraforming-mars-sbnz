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

    private double playerId;
    private boolean requiresEnergy;
    private boolean placesCity;
    private boolean placesGreenery;
    private int raisesTemperature;
    private boolean scienceSynergyBonus;

    public CardInHand(double id, String name, List<CardTag> tags, int cost,
                      int vpValue, String description, double playerId) {
        super(id, name, tags, cost, vpValue, description);
        this.playerId = playerId;
    }
}
