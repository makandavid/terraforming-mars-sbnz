package com.tm.CEP;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TempRaisedEvent extends CEP {

    public TempRaisedEvent(long playerId, long timestamp, int generation) {
        super(playerId, timestamp, generation);
    }
}
