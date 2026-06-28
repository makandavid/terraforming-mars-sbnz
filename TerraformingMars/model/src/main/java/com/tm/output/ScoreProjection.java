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

    private long playerId;
    private int projectedScore;
    private int trContribution;
    private int cardVpContribution;
    private int tileVpContribution;
    private int milestoneVpContribution;
    private int awardVpContribution;
    private String description;

    @Override
    public String toString() {
        return "ScoreProjection[player=" + playerId +
                ", total=" + projectedScore +
                " (TR=" + trContribution +
                ", cards=" + cardVpContribution +
                ", tiles=" + tileVpContribution +
                ", milestones=" + milestoneVpContribution +
                ", awards=" + awardVpContribution + ")]";
    }
}
