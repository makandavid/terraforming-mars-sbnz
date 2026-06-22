package com.tm.facts;

import com.tm.enums.CardTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Card {

    private long id;
    private String name;
    private List<CardTag> tags;
    private int cost;
    private int vpValue;
    private String description;

    public boolean hasTag(CardTag tag) {
        return tags != null && tags.contains(tag);
    }

    public int getTagCount() {
        return tags == null ? 0 : tags.size();
    }

    public long countTag(CardTag tag) {
        if (tags == null) return 0;
        return tags.stream().filter(t -> t == tag).count();
    }
}
