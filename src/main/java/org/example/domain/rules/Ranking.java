package org.example.domain.rules;

public enum Ranking {
    HIGH_CARD(1),
    ONE_PAIR(15*2),
    TWO_PAIR(15*6),
    TRIPLE(15*10),
    STRAIGHT(15*14),
    FLUSH(15*18),
    FULL_HOUSE(15*22),
    FOUR_CARDS(15*26),
    STRAIGHT_FLUSH(15*30);

    private int value;
    private Ranking(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
