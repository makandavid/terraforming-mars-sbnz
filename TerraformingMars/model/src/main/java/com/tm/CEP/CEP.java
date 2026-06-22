package com.tm.CEP;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class CEP {

    private long playerId;
    private long timestamp;
    private int generation;

    public CEP(long playerId, int generation) {
        this.playerId = playerId;
        this.generation = generation;
        this.timestamp = generation * 1000L;
    }
}
