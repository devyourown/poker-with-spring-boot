package org.example.domain.rules;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;
import org.example.domain.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RankingCalculatorTest {
    private List<Card> cards;

    @BeforeEach
    void createCards() {
        cards = new ArrayList<>();
    }

    @Test
    void testOnePair() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(1, Suit.CLUBS)));
        assertEquals(Ranking.ONE_PAIR, RankingCalculator.calculateCards(cards));
    }

    @Test
    void testTwoPair() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(2, Suit.DIAMONDS), Card.of(1, Suit.CLUBS)));
        assertEquals(Ranking.TWO_PAIR, RankingCalculator.calculateCards(cards));
    }

    @Test
    void testTriple() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(1, Suit.CLUBS)));
        assertEquals(Ranking.TRIPLE, RankingCalculator.calculateCards(cards));
    }

    @Test
    void testStraight() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(5, Suit.CLUBS),
                Card.of(3, Suit.DIAMONDS), Card.of(3, Suit.HEARTS)));
        assertEquals(Ranking.STRAIGHT, RankingCalculator.calculateCards(cards));

        cards = new ArrayList<>();

        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(5, Suit.CLUBS)));
        assertEquals(Ranking.STRAIGHT, RankingCalculator.calculateCards(cards));
    }

    @Test
    void testMountain() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(10, Suit.CLUBS),
                Card.of(11, Suit.CLUBS), Card.of(12, Suit.CLUBS), Card.of(13, Suit.CLUBS)));
        assertEquals(Ranking.MOUNTAIN, RankingCalculator.calculateCards(cards));
        cards.clear();
        cards.addAll(List.of(Card.of(9, Suit.HEARTS), Card.of(10, Suit.CLUBS),
                Card.of(11, Suit.CLUBS), Card.of(12, Suit.CLUBS), Card.of(13, Suit.CLUBS)));
        assertNotEquals(Ranking.MOUNTAIN, RankingCalculator.calculateCards(cards));
    }

    @Test
    void testFlush() {
        cards.addAll(List.of(Card.of(1, Suit.CLUBS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(6, Suit.CLUBS)));
        assertEquals(Ranking.FLUSH, RankingCalculator.calculateCards(cards));

        cards.addAll(List.of(Card.of(10, Suit.CLUBS), Card.of(11, Suit.CLUBS)));
        assertEquals(Ranking.FLUSH, RankingCalculator.calculateCards(cards));
    }

    @Test
    void testFullHouse() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(1, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(4, Suit.CLUBS)));
        assertEquals(Ranking.FULL_HOUSE, RankingCalculator.calculateCards(cards));
    }

    @Test
    void testFourCards() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(1, Suit.CLUBS), Card.of(1, Suit.CLUBS), Card.of(4, Suit.CLUBS)));
        assertEquals(Ranking.FOUR_CARDS, RankingCalculator.calculateCards(cards));
    }

    @Test
    void testStraightFlush() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.DIAMONDS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(5, Suit.CLUBS)));
        cards.add(Card.of(6, Suit.CLUBS));
        cards.add(Card.of(7, Suit.CLUBS));
        assertEquals(Ranking.STRAIGHT_FLUSH, RankingCalculator.calculateCards(cards));
    }

    @Test
    void testRoyalStraightFlush() {
        cards.addAll(List.of(Card.of(1, Suit.CLUBS), Card.of(11, Suit.CLUBS),
                Card.of(10, Suit.CLUBS), Card.of(12, Suit.CLUBS), Card.of(13, Suit.CLUBS)));
        assertEquals(Ranking.ROYAL_STRAIGHT_FLUSH, RankingCalculator.calculateCards(cards));

        cards.clear();
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(11, Suit.CLUBS),
                Card.of(10, Suit.CLUBS), Card.of(12, Suit.CLUBS), Card.of(13, Suit.CLUBS),
                Card.of(9, Suit.CLUBS)));
        assertEquals(Ranking.STRAIGHT_FLUSH, RankingCalculator.calculateCards(cards));

        cards.clear();
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(11, Suit.CLUBS),
                Card.of(10, Suit.CLUBS), Card.of(12, Suit.CLUBS), Card.of(13, Suit.CLUBS),
                Card.of(8, Suit.CLUBS), Card.of(9, Suit.DIAMONDS)));
        assertEquals(Ranking.FLUSH, RankingCalculator.calculateCards(cards));
    }
}