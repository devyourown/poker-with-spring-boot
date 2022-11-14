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
}
