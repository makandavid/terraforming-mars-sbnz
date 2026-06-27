package com.tm.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cards")
@Getter
@Setter
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // Tags stored as comma-separated string: "SCIENCE,BUILDING"
    // Parsed back to List<CardTag> in the mapper
    @Column
    private String tags;

    @Column
    private int cost;

    @Column
    private int vpValue;

    @Column private int mcIncrease;
    @Column private int energyIncrease;
    @Column private int heatIncrease;
    @Column private int steelIncrease;
    @Column private int titaniumIncrease;
    @Column private int plantIncrease;

    @Column private int mcProductionIncrease;
    @Column private int energyProductionIncrease;
    @Column private int heatProductionIncrease;
    @Column private int steelProductionIncrease;
    @Column private int titaniumProductionIncrease;
    @Column private int plantProductionIncrease;

    @Column private int temperatureIncrease;
    @Column private int oxygenIncrease;
    @Column private int oceansPlaced;
    @Column private int terraformRatingIncrease;

    @Column private boolean placesCity;
    @Column private boolean placesGreenery;
    @Column private boolean raisesTemperature;
    @Column private boolean scienceSynergyBonus;

    @Column private boolean requiresEnergy;
    @Column private int minTemperature;
    @Column private int maxTemperature;
    @Column private int minOxygen;
    @Column private int maxOxygen;
    @Column private int minOceansPlaced;
    @Column private int maxOceansPlaced;
    @Column private int minGreeneryTiles;
    @Column private int maxGreeneryTiles;
    @Column private int requiredScienceTags;
    @Column private int requiredJovianTags;
    @Column private int requiredPlantTags;
    @Column private int requiredMicrobeTags;
    @Column private int requiredAnimalTags;
}
