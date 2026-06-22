package com.tm.output;

import com.tm.enums.Priority;
import com.tm.enums.RecommendationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {

    private long playerId;
    private RecommendationType type;
    private Priority priority;
    private String subject;
    private String explanation;

    @Override
    public String toString() {
        return "[" + priority + "] " + type + " -> " + subject + " | " + explanation;
    }
}
