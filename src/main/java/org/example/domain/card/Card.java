package org.example.domain.card;

public class Card {
    private final int value;
    private final Suit suit;
    public Card(int value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    public int getValue() {
        return value;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean isSameValue(Card another) {
        return value == another.getValue();
    }

    public boolean isSameSuit(Card another) {
        return suit == another.getSuit();
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
}
