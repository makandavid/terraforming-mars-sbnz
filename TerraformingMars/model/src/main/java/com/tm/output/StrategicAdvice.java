package com.tm.output;

import com.tm.enums.AdviceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StrategicAdvice {

    private double playerId;
    private AdviceType adviceType;
    private String explanation;
}
