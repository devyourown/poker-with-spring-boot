package org.example.domain.game;

import org.example.domain.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        List<Player> players1 = new ArrayList<>();
        for (int i=0; i<4; i++) {
            players1.add(new Player(1000));
        }

        Game game = new Game(players1, 100, 200);
        assertEquals(GameStatus.PRE_FLOP, game.getStatus());
        game.playAction(0, Action.CALL, 0);
        game.playAction(1, Action.CALL, 0);
        game.playAction(2, Action.BET, 300);
        game.playAction(3, Action.CALL, 0);
        game.playAction(0, Action.FOLD, 0);
        game.playAction(0, Action.CALL, 0);
        assertEquals(GameStatus.FLOP, game.getStatus());

        game.playAction(0, Action.CHECK, 0);
        game.playAction(1, Action.BET, 300);
        game.playAction(2, Action.CALL, 0);
        game.playAction(0, Action.FOLD, 0);
        assertEquals(GameStatus.TURN, game.getStatus());

        game.playAction(0, Action.FOLD, 0);
        assertEquals(GameStatus.END, game.getStatus());
    }

    @Test
    void testCheck() throws Exception {
        game.playAction(0, Action.CALL, 0);
        game.playAction(1, Action.CALL, 0);
        game.playAction(2, Action.CHECK, 0);
        assertEquals(game.getPot(), 600);
    }

    @Test
    void testBet() throws Exception {
        game.playAction(0, Action.BET, 300);
        game.playAction(1, Action.CALL, 0);
        game.playAction(2, Action.CALL, 0);
        assertEquals(game.getPot(), 900);
        game.playAction(0, Action.CHECK, 0);
        assertEquals(game.getPot(), 900);
    }

    @Test
    void testStatus() throws Exception {
        assertEquals(game.getStatus(), GameStatus.PRE_FLOP);
        game.playAction(0, Action.BET, 300);
        game.playAction(1, Action.CALL, 0);
        game.playAction(2, Action.CALL, 0);

        assertEquals(game.getStatus(), GameStatus.FLOP);
        game.playAction(0, Action.CHECK, 300);
        assertEquals(game.getStatus(), GameStatus.FLOP);
        game.playAction(1, Action.CHECK, 0);
        assertEquals(game.getStatus(), GameStatus.FLOP);
        game.playAction(2, Action.CHECK, 0);
        assertEquals(game.getStatus(), GameStatus.TURN);

        game.playAction(0, Action.CHECK, 300);
        game.playAction(1, Action.CHECK, 0);
        assertEquals(game.getStatus(), GameStatus.TURN);
        game.playAction(2, Action.CHECK, 0);
        assertEquals(game.getStatus(), GameStatus.RIVER);

        game.playAction(0, Action.CHECK, 300);
        game.playAction(1, Action.CHECK, 0);
        assertEquals(game.getStatus(), GameStatus.RIVER);
        game.playAction(2, Action.CHECK, 0);
        assertEquals(game.getStatus(), GameStatus.END);
    }

    @Test
    void testGameResult() throws Exception {
        game.playAction(0, Action.BET, 300);
        game.playAction(1, Action.FOLD, 0);
        game.playAction(1, Action.FOLD, 0);

        assertTrue(game.isEnd());
        assertEquals(1300, players.get(0).getMoney());
    }
}