package com.tm.CEP;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TempRaisedEvent extends CEP {

    public TempRaisedEvent(long playerId, int generation) {
        super(playerId, generation);
    }
}
