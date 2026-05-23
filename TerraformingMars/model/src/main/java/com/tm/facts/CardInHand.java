package com.tm.facts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardInHand extends Card {

    private double playerId;
    private boolean requiresEnergy;
    private boolean placesCity;
    private boolean placesGreenery;
    private int raisesTemperature;
    private boolean scienceSynergyBonus;
}
