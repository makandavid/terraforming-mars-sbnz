package com.tm.facts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerState {

    private double id;
    private boolean currentPlayer;
    private int terraformRating;
    private int megacredits;
    private int mcProduction;
    private int steel;
    private int steelProduction;
    private int titanium;
    private int titaniumProduction;
    private int energy;
    private int energyProduction;
    private int heat;
    private int heatProduction;
    private int plants;
    private int plantProduction;
    private int cityCount;
    private int greeneryCount;
    private int scienceTagCount;
    private int buildingTagCount;
}
