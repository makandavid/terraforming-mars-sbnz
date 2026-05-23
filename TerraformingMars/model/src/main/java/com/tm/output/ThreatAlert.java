package com.tm.output;

import com.tm.enums.ThreatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThreatAlert {

    private double targetPlayerId;
    private ThreatType type;
    private String description;
}
