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

    public boolean isSameAs(Card another) {
        return value == another.getValue();
    }
    public boolean isHigherThan(Card another) {
        if (isSameAs(another))
            return false;
        if (isAce())
            return true;
        return value > another.getValue();
    }

    public boolean isAce() {
        return value == 1;
    }
}
