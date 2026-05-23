package com.tm.CEP;

import com.tm.enums.TileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TilePlayedEvent extends CEP {

    private TileType tileType;
}
