package com.tm.output;

import com.tm.enums.MilestoneType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneReport {

    private double playerId;
    private MilestoneType milestoneType;
    private boolean reachableNow;
}
