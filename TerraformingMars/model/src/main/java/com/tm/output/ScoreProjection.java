package com.tm.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreProjection {

    private double playerId;
    private int trPoints;
    private int cardVP;
    private int milestoneVP;
    private int totalProjected;
}
