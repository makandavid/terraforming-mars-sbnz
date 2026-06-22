package com.tm.utils;

import com.tm.enums.CardTag;
import com.tm.facts.PlayedCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryUtils {

    public static int getMaxOpponentScienceTags(
            long currentPlayerId,
            List<PlayedCard> playedCards) {

        Map<Long, Integer> counts = new HashMap<>();

        for (PlayedCard card : playedCards) {
            if (card.getPlayerId() == currentPlayerId) {
                continue;
            }

            if (card.getTags().contains(CardTag.SCIENCE)) {
                counts.merge(card.getPlayerId(), 1, Integer::sum);
            }
        }

        return counts.values()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);
    }
}
