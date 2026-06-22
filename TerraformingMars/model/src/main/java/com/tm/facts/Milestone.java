package com.tm.facts;

import com.tm.enums.MilestoneType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Milestone {

    private MilestoneType type;
    private boolean claimed;
    private long claimedBy;
    private int generation;
}
