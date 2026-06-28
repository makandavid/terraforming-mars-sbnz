package com.tm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreProjectionDto {
    private int projectedScore;
    private int trContribution;
    private int cardVpContribution;
    private int tileVpContribution;
    private int milestoneVpContribution;
    private int awardVpContribution;
    private String description;
}
