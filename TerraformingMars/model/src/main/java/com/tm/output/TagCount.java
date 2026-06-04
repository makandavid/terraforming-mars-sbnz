package com.tm.output;

import com.tm.enums.CardTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagCount {

    private double playerId;
    private CardTag tag;
    private int count;

    @Override
    public String toString() {
        return "TagCount[player=" + playerId + ", tag=" + tag + ", count=" + count + "]";
    }
}
