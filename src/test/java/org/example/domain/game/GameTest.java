package org.example.domain.game;

import org.example.domain.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    List<Player> players;
    Game game;

    @BeforeEach
    void setup() {
        players = List.of(new Player("1", 1000),
                new Player("2", 2000), new Player("3", 3000));
        game = new Game(players, 100, 200);
    }

    @Test
    void testCall() throws Exception {
        game.playAction(Action.CALL, 0);
        assertEquals(game.getPot(), 500);
        game.playAction(Action.CALL, 0);
        assertEquals(game.getPot(), 600);
        game.playAction(Action.CALL, 0);
        assertEquals(game.getPot(), 600);
    }

    @Test
    void testFold() throws Exception {
        List<Player> players1 = new ArrayList<>();
        for (int i=0; i<4; i++) {
            players1.add(new Player("4", 1000));
        }

        Game game = new Game(players1, 100, 200);
        assertEquals(4, game.getSizeOfPlayers());
        assertEquals(GameStatus.PRE_FLOP, game.getStatus());
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.BET, 300);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.FOLD, 0);
        game.playAction(Action.CALL, 0);
        assertEquals(GameStatus.FLOP, game.getStatus());

        game.playAction(Action.CHECK, 0);
        game.playAction(Action.BET, 300);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.FOLD, 0);
        assertEquals(GameStatus.TURN, game.getStatus());

        game.playAction(Action.FOLD, 0);
        assertEquals(GameStatus.END, game.getStatus());
    }

    @Test
    void testBetFold() throws Exception {
        game.playAction(Action.BET, 0);
        game.playAction(Action.FOLD, 300);
        assertEquals(GameStatus.PRE_FLOP, game.getStatus());
        game.playAction(Action.CALL, 0);
        assertEquals(GameStatus.FLOP, game.getStatus());
    }

    @Test
    void testCheck() throws Exception {
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CHECK, 0);
        assertEquals(game.getPot(), 600);
    }

    @Test
    void testBet() throws Exception {
        game.playAction(Action.BET, 300);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CALL, 0);
        assertEquals(game.getPot(), 900);
        game.playAction(Action.CHECK, 0);
        assertEquals(game.getPot(), 900);
    }

    @Test
    void testStatus() throws Exception {
        assertEquals(game.getStatus(), GameStatus.PRE_FLOP);
        game.playAction(Action.BET, 300);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CALL, 0);

        assertEquals(game.getStatus(), GameStatus.FLOP);
        game.playAction(Action.CHECK, 300);
        assertEquals(game.getStatus(), GameStatus.FLOP);
        game.playAction(Action.CHECK, 0);
        assertEquals(game.getStatus(), GameStatus.FLOP);
        game.playAction(Action.CHECK, 0);
        assertEquals(game.getStatus(), GameStatus.TURN);

        game.playAction(Action.CHECK, 300);
        game.playAction(Action.CHECK, 0);
        assertEquals(game.getStatus(), GameStatus.TURN);
        game.playAction(Action.CHECK, 0);
        assertEquals(game.getStatus(), GameStatus.RIVER);

        game.playAction(Action.CHECK, 300);
        game.playAction(Action.CHECK, 0);
        assertEquals(game.getStatus(), GameStatus.RIVER);
        game.playAction(Action.CHECK, 0);
        assertEquals(game.getStatus(), GameStatus.END);
    }

    @Test
    void testGameResult() throws Exception {
        game.playAction(Action.BET, 300);
        game.playAction(Action.FOLD, 0);
        game.playAction(Action.FOLD, 0);

        assertTrue(game.isEnd());
        assertEquals(1300, players.get(0).getMoney());
        assertEquals(1900, players.get(1).getMoney());
        assertEquals(2800, players.get(2).getMoney());
    }

    @Test
    void testResetGame() throws Exception {
        game.playAction(Action.BET, 300);
        game.playAction(Action.FOLD, 0);
        game.playAction(Action.FOLD, 0);

        assertTrue(game.isEnd());
        assertEquals(0, game.getPot());
        game.resetGame();
        assertEquals(1300, players.get(0).getMoney());
        assertEquals(1800, players.get(1).getMoney());
        assertEquals(2600, players.get(2).getMoney());

        game.playAction(Action.BET, 300);
        game.playAction(Action.FOLD, 0);
        game.playAction(Action.FOLD, 0);
        assertTrue(game.isEnd());

        assertEquals(1600, players.get(0).getMoney());
        assertEquals(1800, players.get(1).getMoney());
        assertEquals(2600, players.get(2).getMoney());
    }
}