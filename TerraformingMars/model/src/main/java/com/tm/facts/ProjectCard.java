package com.tm.facts;

import com.tm.enums.CardTag;
import com.tm.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectCard extends Card {

    private CardTag synergyTag;
    private int synergyThreshold;
    private Priority recommendedPriority;
    private String recommendedText;

    public ProjectCard(long id, String name, List<CardTag> tags, int cost,
                       int vpValue, String description,
                       CardTag synergyTag, int synergyThreshold, Priority recommendedPriority, String recommendedText) {
        super(id, name, tags, cost, vpValue, description);
        this.synergyTag = synergyTag;
        this.synergyThreshold = synergyThreshold;
        this.recommendedPriority = recommendedPriority;
        this.recommendedText = recommendedText;
    }
}
