package com.tm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TilePlayedEventDto {
    private long playerId;
    private String tileType;
    private int generation;
}
