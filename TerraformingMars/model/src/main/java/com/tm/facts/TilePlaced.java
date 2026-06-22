package com.tm.facts;

import com.tm.enums.TileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TilePlaced {

    private long playerId;
    private TileType tileType;
    private int generation;
}
