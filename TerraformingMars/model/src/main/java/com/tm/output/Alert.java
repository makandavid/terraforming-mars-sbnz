package com.tm.output;

import com.tm.enums.AlertType;
import com.tm.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    private double playerId;
    private AlertType type;
    private Priority priority;
    private String description;
}
