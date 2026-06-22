package com.tm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsedCards {

    private List<Long> ids;

    public UsedCards() {
        this.ids = new ArrayList<>();
    }

    public UsedCards(List<Long> ids) {
        this.ids = ids;
    }

    public boolean contains(long id) {
        return ids.contains(id);
    }

    public UsedCards withAdded(long id) {
        List<Long> newIds = new ArrayList<>(ids);
        newIds.add(id);
        return new UsedCards(newIds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsedCards)) return false;
        UsedCards other = (UsedCards) o;
        return Objects.equals(this.ids, other.ids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids);
    }

    @Override
    public String toString() {
        return "UsedCards" + ids;
    }
}