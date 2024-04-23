package org.example.domain.game;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;
import org.example.domain.deck.DeterminedDeck;
import org.example.domain.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    List<Player> players;
    Game game;

    @Test
    void testCall() throws Exception {
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CALL, 0);
    }

    @Test
    void testFold() throws Exception {
        List<Player> players1 = new ArrayList<>();
        for (int i=0; i<4; i++) {
            players1.add(new Player("4", 1000));
        }

        List<Card> cards = List.of(Card.of(1, Suit.SPADES));
        Game game = new Game(players1, 100, 200, new DeterminedDeck(cards));
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.BET, 300);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.FOLD, 0);
        game.playAction(Action.CALL, 0);

        game.playAction(Action.CHECK, 0);
        game.playAction(Action.BET, 300);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.FOLD, 0);

        game.playAction(Action.FOLD, 0);
    }

    @Test
    void testBetFold() throws Exception {
        game.playAction(Action.BET, 0);
        game.playAction(Action.FOLD, 300);
        game.playAction(Action.CALL, 0);
    }

    @Test
    void testCheck() throws Exception {
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CHECK, 0);
    }

    @Test
    void testBet() throws Exception {
        game.playAction(Action.BET, 300);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CHECK, 0);
    }

    @Test
    void testStatus() throws Exception {
        game.playAction(Action.BET, 300);
        game.playAction(Action.CALL, 0);
        game.playAction(Action.CALL, 0);

        game.playAction(Action.CHECK, 300);
        game.playAction(Action.CHECK, 0);
        game.playAction(Action.CHECK, 0);

        game.playAction(Action.CHECK, 300);
        game.playAction(Action.CHECK, 0);
        game.playAction(Action.CHECK, 0);

        game.playAction(Action.CHECK, 300);
        game.playAction(Action.CHECK, 0);
        game.playAction(Action.CHECK, 0);
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
        game.resetGame();
        assertEquals(1200, players.get(0).getMoney());
        assertEquals(1700, players.get(1).getMoney());
        assertEquals(2800, players.get(2).getMoney());

        game.playAction(Action.BET, 300);
        game.playAction(Action.FOLD, 0);
        game.playAction(Action.FOLD, 0);
        assertTrue(game.isEnd());

        assertEquals(1200, players.get(0).getMoney());
        assertEquals(1700, players.get(1).getMoney());
        assertEquals(3100, players.get(2).getMoney());
    }
}