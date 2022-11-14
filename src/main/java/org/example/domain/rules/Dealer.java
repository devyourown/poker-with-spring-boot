package org.example.domain.rules;

import org.example.domain.card.Card;

import java.util.List;

public class Dealer {
    public static HandRanking calculateCards(List<Card> cards) {
        return HandRanking.ONE_PAIR;
    }

    public static HandRanking calculateHands(List<Card> hands) {
        if (hands.get(0).isSameAs(hands.get(1)))
            return HandRanking.ONE_PAIR;
        return HandRanking.HIGH_CARD;
    }
}
