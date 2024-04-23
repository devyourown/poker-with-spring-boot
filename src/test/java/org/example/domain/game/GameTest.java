package org.example.domain.game;

import org.poker.domain.card.Card;
import org.poker.domain.card.Suit;
import org.poker.domain.deck.Deck;
import org.poker.domain.deck.DeterminedDeck;
import org.poker.domain.game.Action;
import org.poker.domain.game.Game;
import org.poker.domain.game.helper.GameResult;
import org.poker.domain.player.Player;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    List<Player> players;
    Game game;
    Scanner scanner;

    private static InputStream generateUserInput(String input) {
        return new ByteArrayInputStream(input.getBytes());
    }

    private static InputStream generateUserInputs(String input) {
        List<InputStream> streams = new ArrayList<>();
        for (String token : input.split("/")) {
            streams.add(generateUserInput(token));
        }
        return new SequenceInputStream(Collections.enumeration(streams));
    }

    @Test
    void testNormalEnd() {
        players = List.of(
                new Player("Good", 100000),
                new Player("Jake", 100000),
                new Player("Cacke", 1000000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(13, Suit.SPADES), Card.of(1, Suit.SPADES),/*Cacke*/
                Card.of(7, Suit.HEARTS), Card.of(11, Suit.SPADES),/*Jake*/
                Card.of(3, Suit.SPADES), Card.of(13, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        game = new Game(players, 100, 200, deck);
        InputStream in = generateUserInputs(
                "CALL\n/CALL\n/CALL\n/CHECK\n/CHECK\n/CHECK\n/" +
                        "CHECK\n/CHECK\n/CHECK\n/CHECK\n/CHECK\n/CHECK\n");
        System.setIn(in);
        scanner = new Scanner(System.in);
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("Winner : Cacke"));
    }
}