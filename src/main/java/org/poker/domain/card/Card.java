package org.poker.domain.card;

import java.util.HashMap;
import java.util.Map;

public class Card {
    private static final Map<String, Card> cache = new HashMap<>();
    private final int value;
    private final Suit suit;

    private Card(int value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    public static Card of(int value, Suit suit) {
        String key = suit.toString() + value;
        if (!cache.containsKey(key))
            cache.put(key, new Card(value, suit));
        return cache.get(key);
    }

    public int getValue() {
        if (isAce())
            return 14;
        return this.value;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean isSameValue(Card another) {
        return value == another.getValue();
    }

    public boolean isHigherThan(Card another) {
        if (isSameValue(another))
            return false;
        if (isAce())
            return true;
        return value > another.getValue();
    }

    public boolean isAce() {
        return value == 1;
    }

    public String toString() {
        return suit.toString() + " " + value;
    }
}
