package org.poker.domain.rules;

public enum Ranking {
    FOLD(0),
    HIGH_CARD(1),
    ONE_PAIR((long)Math.pow(15, 2)),
    TWO_PAIR((long)Math.pow(15, 3)),
    TRIPLE((long)Math.pow(15, 4)),
    STRAIGHT((long)Math.pow(15, 5)),
    FLUSH((long)Math.pow(15, 6)),
    FULL_HOUSE((long)Math.pow(15, 7)),
    FOUR_CARDS((long)Math.pow(15, 8)),
    STRAIGHT_FLUSH((long)Math.pow(15, 9));

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
