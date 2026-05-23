package com.tm.facts;

import com.tm.enums.CardTag;
import com.tm.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCard extends Card {

    private CardTag synergyTag;
    private int synergyThreshold;
    private Priority recommendedPriority;
    private String recommendedText;
}
