package com.tm.output;

import com.tm.enums.InsightType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Insight {

    private double playerId;
    private InsightType type;
    private String description;
}
