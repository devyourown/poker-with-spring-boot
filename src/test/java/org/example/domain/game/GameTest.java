package org.example.domain.game;

import org.example.domain.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    List<Player> players;
    Game game;

    @BeforeEach
    void setup() {
        players = List.of(new Player(1000),
                new Player(2000), new Player(3000));
        game = new Game(players, 100, 200);
    }

    @Test
    void testCall() throws Exception {
        game.playAction(0, Action.CALL, 0);
        assertEquals(game.getPot(), 500);
        game.playAction(1, Action.CALL, 0);
        assertEquals(game.getPot(), 600);
        game.playAction(2, Action.CALL, 0);
        assertEquals(game.getPot(), 600);
    }

    @Test
    void testFold() throws Exception {
        game.playAction(0, Action.FOLD, 0);
        game.playAction(1, Action.FOLD, 0);
        assertEquals(game.isEnd(), true);
    }

    @Test
    void testCheck() throws Exception {
        game.playAction(0, Action.CALL, 0);
        game.playAction(1, Action.CALL, 0);
        game.playAction(2, Action.CHECK, 0);
        assertEquals(game.getPot(), 600);
    }
}