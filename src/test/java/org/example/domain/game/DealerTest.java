package org.example.domain.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void testGame() {
        Game game = new Game(3);
        game.getPlayers();
        game.handOut();
    }
}