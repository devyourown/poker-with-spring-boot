package org.example.domain.game;

public enum GameStatus {
    PRE_FLOP,
    FLOP,
    TURN,
    RIVER,
    END;

    public GameStatus nextStatus() {
        return GameStatus.values()[this.ordinal() + 1];
    }
}
