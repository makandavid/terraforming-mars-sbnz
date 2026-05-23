package com.tm.CEP;

import com.tm.enums.CardTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardPlayedEvent extends CEP {

    private CardTag cardTag;
}
