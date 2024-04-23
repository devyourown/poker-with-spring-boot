package org.example.domain.rules;

public enum Ranking {
    HIGH_CARD(1),
    ONE_PAIR(60),
    TWO_PAIR(60*3),
    TRIPLE(60*25),
    STRAIGHT(60*30),
    FLUSH(60*35),
    FULL_HOUSE(60*40),
    FOUR_CARDS(60*45),
    STRAIGHT_FLUSH(60*50);

    private int value;
    private Ranking(int value) {
        this.value = value;
    }

    public static Ranking getRank(int value) {
        for (Ranking ranking : Ranking.values()) {
            if (Ranking.values()[ranking.ordinal()+1].getValue() > value && value >= ranking.getValue()) {
                return ranking;
            }
        }
        return STRAIGHT_FLUSH;
    }

    public int getValue() {
        return this.value;
    }
}
