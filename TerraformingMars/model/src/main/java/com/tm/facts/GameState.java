package com.tm.facts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameState {

    private int generation;
    private double oxygenLevel;
    private double temperature;
    private int oceanCount;
    private int lastTempRaisedGeneration;
}
