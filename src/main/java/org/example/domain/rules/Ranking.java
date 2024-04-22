package org.example.domain.rules;

public enum Ranking {
    HIGH_CARD(1),
    ONE_PAIR(15*2),
    TWO_PAIR(15*5),
    TRIPLE(15*25),
    STRAIGHT(15*30),
    FLUSH(15*35),
    FULL_HOUSE(15*40),
    FOUR_CARDS(15*45),
    STRAIGHT_FLUSH(15*50);

    private int value;
    private Ranking(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
