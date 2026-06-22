package com.tm.CEP;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class CEP {

    private long playerId;
    private long timestamp;
    private int generation;
}
