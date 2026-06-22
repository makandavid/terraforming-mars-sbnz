package com.tm.facts;

import com.tm.enums.AwardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Award {

    private AwardType type;
    private boolean funded;
    private long fundedBy;
    private int cost;
}
