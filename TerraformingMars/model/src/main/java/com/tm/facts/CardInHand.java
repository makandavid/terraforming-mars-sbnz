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

    // --- Efekti na produkciju (koliko se menja po generaciji) ---
    private int mcProductionIncrease;       // povecanje MC produkcije
    private int energyProductionIncrease;   // povecanje energetske produkcije
    private int heatProductionIncrease;     // povecanje produkcije toplote
    private int steelProductionIncrease;    // povecanje produkcije celika
    private int titaniumProductionIncrease; // povecanje produkcije titanijuma
    private int plantProductionIncrease;    // povecanje produkcije biljaka

    // --- Efekti na globalne parametre ---
    private int temperatureIncrease;  // koliko stepeni podiZe temperaturu
    private int oxygenIncrease;       // koliko % podiZe kiseonik
    private int oceansPlaced;         // koliko okeana postavlja

    // --- Efekti na plocice ---
    private boolean placesCity;
    private boolean placesGreenery;

    // --- Preduslovi za igranje ---
    private boolean requiresEnergy;          // zahteva energiju za aktivaciju
    private int minTemperature;              // minimalna temperatura za igranje
    private int minOxygen;                   // minimalni kiseonik za igranje
    private int requiredScienceTags;         // min broj science tagova

    // --- Sinergije relevantne za pravila ---
    private boolean scienceSynergyBonus;     // ima bonus uz science tagove
    private boolean raisesTemperature;       // podiZe temperaturu kao efekat

    public CardInHand(double id, String name, List<CardTag> tags, int cost,
                      int vpValue, String description, double playerId) {
        super(id, name, tags, cost, vpValue, description);
        this.playerId = playerId;
    }
}
