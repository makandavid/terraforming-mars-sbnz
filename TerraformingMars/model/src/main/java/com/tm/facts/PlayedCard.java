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
public class PlayedCard extends Card {

    private double playerId;
    private int generationPlayed;

    public PlayedCard(double id, String name, List<CardTag> tags, int cost,
                      int vpValue, String description,
                      double playerId, int generationPlayed) {
        super(id, name, tags, cost, vpValue, description);
        this.playerId = playerId;
        this.generationPlayed = generationPlayed;
    }
}
