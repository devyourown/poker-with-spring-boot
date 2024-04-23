package org.example.domain.rules;

public enum Ranking {
    HIGH_CARD(1),
    ONE_PAIR(15),
    TWO_PAIR((int)Math.pow(15, 2)),
    TRIPLE((int)Math.pow(15, 3)),
    STRAIGHT((int)Math.pow(15, 4)),
    FLUSH((int)Math.pow(15, 5)),
    FULL_HOUSE((int)Math.pow(15, 6)),
    FOUR_CARDS((int)Math.pow(15, 7)),
    STRAIGHT_FLUSH((int)Math.pow(15, 8));

    private long value;
    private Ranking(long value) {
        this.value = value;
    }

    public static Ranking getRank(long value) {
        for (Ranking ranking : Ranking.values()) {
            if (Ranking.values().length <= ranking.ordinal()+1)
                return ranking;
            if (Ranking.values()[ranking.ordinal()+1].getValue() > value && value >= ranking.getValue()) {
                return ranking;
            }
        }
        return STRAIGHT_FLUSH;
    }

    public long getValue() {
        return this.value;
    }
}
