package com.tm.CEP;

import com.tm.enums.CardTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardPlayedEvent extends CEP {

    private CardTag cardTag;

    public CardPlayedEvent(long playerId, int generation, CardTag cardTag) {
        super(playerId, generation);
        this.cardTag = cardTag;
    }
}
