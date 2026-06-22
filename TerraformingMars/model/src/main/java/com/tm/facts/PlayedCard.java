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

    private long playerId;
    private int generationPlayed;

    public PlayedCard(long id, String name, List<CardTag> tags, int cost,
                      int vpValue, String description,
                      long playerId, int generationPlayed) {
        super(id, name, tags, cost, vpValue, description);
        this.playerId = playerId;
        this.generationPlayed = generationPlayed;
    }
}
