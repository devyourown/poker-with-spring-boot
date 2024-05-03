package org.example.domain.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.poker.console.ConsoleInput;
import org.poker.console.ConsoleOutput;
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
    void testWhenEveryoneFold() {
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
        InputStream in = generateUserInputs(
                "FOLD\n/FOLD\n");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("Winner : Cacke"));
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
        InputStream in = generateUserInputs(
                "CALL\n/CALL\n/CALL\n/CHECK\n/CHECK\n/CHECK\n/" +
                        "CHECK\n/CHECK\n/CHECK\n/CHECK\n/CHECK\n/CHECK\n");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck, 
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("Winner : Cacke"));
    }

    @Test
    void testWhenSmallerAllin() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 4000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(1, Suit.SPADES), Card.of(1, Suit.SPADES),/*Cacke*/
                Card.of(10, Suit.HEARTS), Card.of(10, Suit.SPADES),/*Jake*/
                Card.of(3, Suit.SPADES), Card.of(3, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                        "BET\n/2000\n/BET\n/2900\n/CALL\n");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("Winner : Cacke"));
    }

    @Test
    void testWhenSmallerAllinAndWin() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 4000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(3, Suit.SPADES), Card.of(3, Suit.SPADES),/*Cacke*/
                Card.of(10, Suit.HEARTS), Card.of(10, Suit.SPADES),/*Jake*/
                Card.of(1, Suit.SPADES), Card.of(1, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/2000\n/BET\n/2900\n/CALL\n/");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 6000"));
        assertTrue(resultInString.contains("ID: Jake, money: 2000"));
        assertTrue(resultInString.contains("ID: Cacke, money: 1000"));
    }

    @Test
    void testWhenMediumWin() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 4000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(3, Suit.SPADES), Card.of(3, Suit.SPADES),/*Cacke*/
                Card.of(1, Suit.HEARTS), Card.of(1, Suit.SPADES),/*Jake*/
                Card.of(10, Suit.SPADES), Card.of(10, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/2000\n/BET\n/2900\n/CALL\n/");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 0"));
        assertTrue(resultInString.contains("ID: Jake, money: 8000"));
        assertTrue(resultInString.contains("ID: Cacke, money: 1000"));
    }

    @Test
    void testWhenChopped() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 4000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(3, Suit.SPADES), Card.of(3, Suit.SPADES),/*Cacke*/
                Card.of(3, Suit.HEARTS), Card.of(3, Suit.SPADES),/*Jake*/
                Card.of(2, Suit.SPADES), Card.of(2, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/2000\n/BET\n/2900\n/CALL\n/");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 0"));
        assertTrue(resultInString.contains("ID: Jake, money: 4000"));
        assertTrue(resultInString.contains("ID: Cacke, money: 5000"));
    }

    @Test
    void testSmallerChop() {
        players = List.of(
                new Player("Good", 400),
                new Player("Jake", 600),
                new Player("Cacke", 10000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(2, Suit.SPADES), Card.of(2, Suit.SPADES),/*Cacke*/
                Card.of(3, Suit.HEARTS), Card.of(3, Suit.SPADES),/*Jake*/
                Card.of(3, Suit.SPADES), Card.of(3, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/400\n/BET\n/500\n/CALL\n/");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 600"));
        assertTrue(resultInString.contains("ID: Jake, money: 1000"));
        assertTrue(resultInString.contains("ID: Cacke, money: 9400"));
    }

    @Test
    void testEqualChop() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 3000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(3, Suit.SPADES), Card.of(3, Suit.SPADES),/*Cacke*/
                Card.of(3, Suit.HEARTS), Card.of(3, Suit.SPADES),/*Jake*/
                Card.of(2, Suit.SPADES), Card.of(2, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/2000\n/BET\n/2900\n/CALL\n/");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 0"));
        assertTrue(resultInString.contains("ID: Jake, money: 4000"));
        assertTrue(resultInString.contains("ID: Cacke, money: 4000"));
    }

    @Test
    void testWhenSmallerChopped() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 4000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(2, Suit.SPADES), Card.of(2, Suit.SPADES),/*Cacke*/
                Card.of(3, Suit.HEARTS), Card.of(3, Suit.SPADES),/*Jake*/
                Card.of(3, Suit.SPADES), Card.of(3, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/2000\n/BET\n/2900\n/CALL\n/");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 3000"));
        assertTrue(resultInString.contains("ID: Jake, money: 5000"));
        assertTrue(resultInString.contains("ID: Cacke, money: 1000"));
    }

    @Test
    void testWhenTopBottomChop() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 4000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(3, Suit.SPADES), Card.of(3, Suit.SPADES),/*Cacke*/
                Card.of(2, Suit.HEARTS), Card.of(2, Suit.SPADES),/*Jake*/
                Card.of(3, Suit.SPADES), Card.of(3, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/2000\n/BET\n/2900\n/CALL\n/");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 3000"));
        assertTrue(resultInString.contains("ID: Jake, money: 0"));
        assertTrue(resultInString.contains("ID: Cacke, money: 6000"));
    }

    @Test
    void testLoserChop() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 4000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(3, Suit.SPADES), Card.of(3, Suit.SPADES),/*Cacke*/
                Card.of(3, Suit.HEARTS), Card.of(3, Suit.SPADES),/*Jake*/
                Card.of(1, Suit.SPADES), Card.of(1, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/2000\n/BET\n/2900\n/CALL\n/");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 6000"));
        assertTrue(resultInString.contains("ID: Jake, money: 1000"));
        assertTrue(resultInString.contains("ID: Cacke, money: 2000"));
    }

    @Test
    void testChopWithLoserWhenBigLoser() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 4000),
                new Player("loser", 4000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(4, Suit.HEARTS), Card.of(5, Suit.SPADES),/*loser*/
                Card.of(3, Suit.SPADES), Card.of(3, Suit.SPADES),/*Cacke*/
                Card.of(3, Suit.HEARTS), Card.of(3, Suit.SPADES),/*Jake*/
                Card.of(1, Suit.SPADES), Card.of(1, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/2000\n/BET\n/3000\n/BET\n/3900\n/CALL\n/");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 8000"));
        assertTrue(resultInString.contains("ID: Jake, money: 2000"));
        assertTrue(resultInString.contains("ID: Cacke, money: 3000"));
        assertTrue(resultInString.contains("ID: loser, money: 0"));
    }

    @Test
    void testWhenBigBetAllFold() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 4000),
                new Player("loser", 4000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(4, Suit.HEARTS), Card.of(5, Suit.SPADES),/*loser*/
                Card.of(3, Suit.SPADES), Card.of(3, Suit.SPADES),/*Cacke*/
                Card.of(3, Suit.HEARTS), Card.of(3, Suit.SPADES),/*Jake*/
                Card.of(1, Suit.SPADES), Card.of(1, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/2000\n/FOLD\n/FOLD\n/FOLD\n");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 2300"));
        assertTrue(resultInString.contains("ID: Jake, money: 3000"));
        assertTrue(resultInString.contains("ID: Cacke, money: 3900"));
        assertTrue(resultInString.contains("ID: loser, money: 3800"));
    }

    @Test
    void testTwoMenShowDown() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 4000),
                new Player("loser", 4000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(4, Suit.HEARTS), Card.of(5, Suit.SPADES),/*loser*/
                Card.of(3, Suit.SPADES), Card.of(3, Suit.SPADES),/*Cacke*/
                Card.of(3, Suit.HEARTS), Card.of(3, Suit.SPADES),/*Jake*/
                Card.of(1, Suit.SPADES), Card.of(1, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/2000\n/CALL\n/FOLD\n/FOLD\n");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 4300"));
        assertTrue(resultInString.contains("ID: Jake, money: 1000"));
        assertTrue(resultInString.contains("ID: Cacke, money: 3900"));
        assertTrue(resultInString.contains("ID: loser, money: 3800"));
    }

    @Test
    void testOneAllinTwoPlay() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 4000),
                new Player("loser", 4000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(4, Suit.HEARTS), Card.of(5, Suit.SPADES),/*loser*/
                Card.of(3, Suit.SPADES), Card.of(3, Suit.SPADES),/*Cacke*/
                Card.of(3, Suit.HEARTS), Card.of(3, Suit.SPADES),/*Jake*/
                Card.of(1, Suit.SPADES), Card.of(1, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/2000\n/CALL\n/CALL\n/FOLD\n" +
                        "BET\n/500\n/CALL\n" +
                "CHECK\n/CHECK\n" +
                "CHECK\n/CHECK\n");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 6200"));
        assertTrue(resultInString.contains("ID: Jake, money: 1000"));
        assertTrue(resultInString.contains("ID: Cacke, money: 2000"));
        assertTrue(resultInString.contains("ID: loser, money: 3800"));
    }

    @Test
    void testOneAllinThreePlay() {
        players = List.of(
                new Player("Good", 2000),
                new Player("Jake", 3000),
                new Player("Cacke", 4000),
                new Player("loser", 4000));
        ArrayList<Card> cards = new ArrayList<>(List.of(Card.of(1, Suit.SPADES),
                Card.of(3, Suit.SPADES), Card.of(7, Suit.DIAMONDS),
                Card.of(10, Suit.CLUBS), Card.of(9, Suit.HEARTS),
                Card.of(4, Suit.HEARTS), Card.of(5, Suit.SPADES),/*loser*/
                Card.of(3, Suit.SPADES), Card.of(3, Suit.SPADES),/*Cacke*/
                Card.of(3, Suit.HEARTS), Card.of(3, Suit.SPADES),/*Jake*/
                Card.of(1, Suit.SPADES), Card.of(1, Suit.HEARTS)));/*Good*/
        Deck deck = new DeterminedDeck(cards);
        InputStream in = generateUserInputs(
                "BET\n/2000\n/CALL\n/CALL\n/CALL\n" +
                        "BET\n/500\n/CALL\n/CALL\n" +
                        "CHECK\n/CHECK\n/CHECK\n" +
                        "CHECK\n/CHECK\n/CHECK\n");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        game = new Game(players, 100, 200, deck,
                new ConsoleInput(scanner),
                new ConsoleOutput());
        GameResult result = game.play();
        String resultInString = result.toString();
        System.out.println(resultInString);
        assertTrue(resultInString.contains("ID: Good, money: 8000"));
        assertTrue(resultInString.contains("ID: Jake, money: 1250"));
        assertTrue(resultInString.contains("ID: Cacke, money: 2250"));
        assertTrue(resultInString.contains("ID: loser, money: 1500"));
    }
}