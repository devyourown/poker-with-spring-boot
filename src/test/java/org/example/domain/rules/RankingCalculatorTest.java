package org.example.domain.rules;

import org.poker.domain.card.Card;
import org.poker.domain.card.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.poker.domain.rules.Ranking;
import org.poker.domain.rules.RankingCalculator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RankingCalculatorTest {
    private List<Card> cards;
    private List<Card> lower;
    private List<Card> higher;

    @BeforeEach
    void createCards() {
        cards = new ArrayList<>();
        lower = new ArrayList<>();
        higher = new ArrayList<>();
    }

    @Test
    void testOnePair() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(1, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        assertEquals(Ranking.ONE_PAIR, Ranking.getRank(RankingCalculator.calculateCards(cards)));
    }

    @Test
    void testOnePairCompare() {
        lower.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(1, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        higher.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(1, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(10, Suit.HEARTS)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));

        lower.clear(); higher.clear();
        higher.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(1, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        lower.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(2, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));
    }

    @Test
    void testTwoPair() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(2, Suit.DIAMONDS), Card.of(1, Suit.CLUBS),
                Card.of(10, Suit.DIAMONDS), Card.of(11, Suit.DIAMONDS)));
        assertEquals(Ranking.TWO_PAIR, Ranking.getRank(RankingCalculator.calculateCards(cards)));
    }

    @Test
    void testTwoPairCompare() {
        lower.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(2, Suit.DIAMONDS), Card.of(1, Suit.CLUBS),
                Card.of(10, Suit.DIAMONDS), Card.of(11, Suit.DIAMONDS)));
        higher.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(2, Suit.DIAMONDS), Card.of(1, Suit.CLUBS),
                Card.of(10, Suit.DIAMONDS), Card.of(13, Suit.DIAMONDS)));
        assertEquals(Ranking.TWO_PAIR, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.TWO_PAIR, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));

        lower.clear();
        higher.clear();
        lower.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(2, Suit.DIAMONDS), Card.of(1, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        higher.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(10, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(1, Suit.CLUBS), Card.of(10, Suit.DIAMONDS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        assertEquals(Ranking.TWO_PAIR, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.TWO_PAIR, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));

        lower.clear();
        higher.clear();
        lower.addAll(List.of(Card.of(12, Suit.HEARTS), Card.of(10, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(10, Suit.CLUBS), Card.of(12, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        higher.addAll(List.of(Card.of(13, Suit.HEARTS), Card.of(10, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(10, Suit.CLUBS), Card.of(13, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        assertEquals(Ranking.TWO_PAIR, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.TWO_PAIR, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));
    }

    @Test
    void testTriple() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(1, Suit.CLUBS),
                Card.of(10, Suit.DIAMONDS), Card.of(11, Suit.DIAMONDS)));
        assertEquals(Ranking.TRIPLE, Ranking.getRank(RankingCalculator.calculateCards(cards)));
    }

    @Test
    void testTripleCompare() {
        lower.addAll(List.of(Card.of(2, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(2, Suit.DIAMONDS), Card.of(1, Suit.CLUBS),
                Card.of(10, Suit.DIAMONDS), Card.of(11, Suit.DIAMONDS)));
        higher.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(2, Suit.DIAMONDS), Card.of(1, Suit.CLUBS),
                Card.of(10, Suit.DIAMONDS), Card.of(13, Suit.DIAMONDS)));
        assertEquals(Ranking.TRIPLE, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.TRIPLE, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));

        lower.clear();
        higher.clear();
        lower.addAll(List.of(Card.of(2, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.DIAMONDS), Card.of(2, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        higher.addAll(List.of(Card.of(2, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(1, Suit.CLUBS), Card.of(2, Suit.DIAMONDS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        assertEquals(Ranking.TRIPLE, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.TRIPLE, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));

        lower.clear();
        higher.clear();
        lower.addAll(List.of(Card.of(2, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(12, Suit.CLUBS), Card.of(1, Suit.CLUBS), Card.of(2, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        higher.addAll(List.of(Card.of(2, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(13, Suit.CLUBS), Card.of(1, Suit.CLUBS), Card.of(2, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        assertEquals(Ranking.TRIPLE, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.TRIPLE, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));
    }

    @Test
    void testStraight() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(5, Suit.CLUBS),
                Card.of(3, Suit.DIAMONDS), Card.of(3, Suit.HEARTS)));
        assertEquals(Ranking.STRAIGHT, Ranking.getRank(RankingCalculator.calculateCards(cards)));

        cards = new ArrayList<>();

        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(5, Suit.CLUBS),
                Card.of(10, Suit.HEARTS), Card.of(11, Suit.HEARTS)));
        assertEquals(Ranking.STRAIGHT, Ranking.getRank(RankingCalculator.calculateCards(cards)));
    }

    @Test
    void testStraightCompare() {
        lower.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.DIAMONDS), Card.of(5, Suit.CLUBS),
                Card.of(10, Suit.DIAMONDS), Card.of(11, Suit.DIAMONDS)));
        higher.addAll(List.of(Card.of(2, Suit.HEARTS), Card.of(3, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.DIAMONDS), Card.of(5, Suit.CLUBS),
                Card.of(10, Suit.DIAMONDS), Card.of(6, Suit.DIAMONDS)));
        assertEquals(Ranking.STRAIGHT, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.STRAIGHT, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));

        lower.clear();
        higher.clear();
        lower.addAll(List.of(Card.of(13, Suit.HEARTS), Card.of(11, Suit.CLUBS),
                Card.of(10, Suit.CLUBS), Card.of(4, Suit.DIAMONDS), Card.of(12, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        higher.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(12, Suit.CLUBS),
                Card.of(11, Suit.CLUBS), Card.of(10, Suit.CLUBS), Card.of(13, Suit.DIAMONDS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        assertEquals(Ranking.STRAIGHT, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.STRAIGHT, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));
    }

    @Test
    void testFlush() {
        cards.addAll(List.of(Card.of(1, Suit.CLUBS), Card.of(2, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(6, Suit.CLUBS),
                Card.of(9, Suit.HEARTS), Card.of(4, Suit.SPADES)));
        assertEquals(Ranking.FLUSH, Ranking.getRank(RankingCalculator.calculateCards(cards)));
    }

    @Test
    void testFlushCompare() {
        lower.addAll(List.of(Card.of(13, Suit.HEARTS), Card.of(11, Suit.CLUBS),
                Card.of(10, Suit.HEARTS), Card.of(4, Suit.HEARTS), Card.of(12, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        higher.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(12, Suit.HEARTS),
                Card.of(11, Suit.HEARTS), Card.of(10, Suit.HEARTS), Card.of(13, Suit.DIAMONDS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        assertEquals(Ranking.FLUSH, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.FLUSH, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));
    }

    @Test
    void testFullHouse() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(1, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(4, Suit.CLUBS),
                Card.of(10, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        assertEquals(Ranking.FULL_HOUSE, Ranking.getRank(RankingCalculator.calculateCards(cards)));
    }

    @Test
    void testFullHouseCompare() {
        lower.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(3, Suit.DIAMONDS), Card.of(5, Suit.CLUBS),
                Card.of(10, Suit.DIAMONDS), Card.of(3, Suit.DIAMONDS)));
        higher.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(1, Suit.DIAMONDS), Card.of(3, Suit.CLUBS),
                Card.of(10, Suit.DIAMONDS), Card.of(6, Suit.DIAMONDS)));
        assertEquals(Ranking.FULL_HOUSE, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.FULL_HOUSE, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));

        lower.clear();
        higher.clear();
        lower.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(12, Suit.CLUBS), Card.of(12, Suit.DIAMONDS), Card.of(1, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        higher.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(13, Suit.CLUBS), Card.of(13, Suit.CLUBS), Card.of(1, Suit.DIAMONDS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        assertEquals(Ranking.FULL_HOUSE, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.FULL_HOUSE, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));

        lower.clear();
        higher.clear();
        lower.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(12, Suit.CLUBS), Card.of(12, Suit.DIAMONDS), Card.of(12, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        higher.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(13, Suit.CLUBS), Card.of(13, Suit.CLUBS), Card.of(13, Suit.DIAMONDS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        assertEquals(Ranking.FULL_HOUSE, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.FULL_HOUSE, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));
    }

    @Test
    void testFourCards() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(1, Suit.CLUBS), Card.of(1, Suit.CLUBS), Card.of(4, Suit.CLUBS)));
        assertEquals(Ranking.FOUR_CARDS, Ranking.getRank(RankingCalculator.calculateCards(cards)));
    }

    @Test
    void testFourCardsCompare() {
        lower.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(3, Suit.CLUBS), Card.of(3, Suit.DIAMONDS), Card.of(5, Suit.CLUBS),
                Card.of(3, Suit.DIAMONDS), Card.of(3, Suit.DIAMONDS)));
        higher.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(1, Suit.CLUBS), Card.of(1, Suit.DIAMONDS), Card.of(3, Suit.CLUBS),
                Card.of(10, Suit.DIAMONDS), Card.of(6, Suit.DIAMONDS)));
        assertEquals(Ranking.FOUR_CARDS, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.FOUR_CARDS, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));

        lower.clear();
        higher.clear();
        lower.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(12, Suit.CLUBS), Card.of(1, Suit.DIAMONDS), Card.of(1, Suit.CLUBS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        higher.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(13, Suit.CLUBS), Card.of(1, Suit.CLUBS), Card.of(1, Suit.DIAMONDS),
                Card.of(7, Suit.HEARTS), Card.of(9, Suit.HEARTS)));
        assertEquals(Ranking.FOUR_CARDS, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.FOUR_CARDS, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));
    }

    @Test
    void testStraightFlush() {
        cards.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(2, Suit.DIAMONDS),
                Card.of(3, Suit.CLUBS), Card.of(4, Suit.CLUBS), Card.of(5, Suit.CLUBS)));
        cards.add(Card.of(6, Suit.CLUBS));
        cards.add(Card.of(7, Suit.CLUBS));
        assertEquals(Ranking.STRAIGHT_FLUSH, Ranking.getRank(RankingCalculator.calculateCards(cards)));
    }

    @Test
    void testStraightFlushCompare() {
        lower.addAll(List.of(Card.of(1, Suit.HEARTS), Card.of(1, Suit.CLUBS),
                Card.of(2, Suit.HEARTS), Card.of(3, Suit.HEARTS), Card.of(5, Suit.CLUBS),
                Card.of(4, Suit.HEARTS), Card.of(5, Suit.HEARTS)));
        higher.addAll(List.of(Card.of(2, Suit.HEARTS), Card.of(10, Suit.CLUBS),
                Card.of(3, Suit.HEARTS), Card.of(5, Suit.HEARTS), Card.of(3, Suit.CLUBS),
                Card.of(4, Suit.HEARTS), Card.of(6, Suit.HEARTS)));
        assertEquals(Ranking.STRAIGHT_FLUSH, Ranking.getRank(RankingCalculator.calculateCards(lower)));
        assertEquals(Ranking.STRAIGHT_FLUSH, Ranking.getRank(RankingCalculator.calculateCards(higher)));
        assertTrue(RankingCalculator.calculateCards(lower) < RankingCalculator.calculateCards(higher));
    }
}