package com.tm.CEP;

import com.tm.enums.TileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TilePlayedEvent extends CEP {

    private TileType tileType;

    public TilePlayedEvent(long playerId, int generation, TileType tileType) {
        super(playerId, generation);
        this.tileType = tileType;
    }
}
