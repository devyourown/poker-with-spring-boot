package org.example.domain.game;

public enum GameStatus {
    PRE_FLOP,
    FLOP,
    TURN,
    RIVER,
    END;

    public GameStatus nextStatus() {
        if (this.ordinal() == 4)
            return END;
        return GameStatus.values()[this.ordinal() + 1];
    }
}
