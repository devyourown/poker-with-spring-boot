package org.example.domain.rules;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RankingSeparatorTest {
    private List<Card> cards;
    @BeforeEach
    void createCards() {
        cards = new ArrayList<>();
    }

    @Test
    void testOnePair() {
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(1, Suit.CLUBS)));
        assertEquals(HandRanking.ONE_PAIR, RankingSeparator.calculateCards(cards));
    }

    @Test
    void testTwoPair() {
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(2, Suit.DIAMONDS), new Card(1, Suit.CLUBS)));
        assertEquals(HandRanking.TWO_PAIR, RankingSeparator.calculateCards(cards));
    }

    @Test
    void testTriple() {
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(1, Suit.CLUBS)));
        assertEquals(HandRanking.TRIPLE, RankingSeparator.calculateCards(cards));
    }

    @Test
    void testStraight() {
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(5, Suit.CLUBS),
                new Card(3, Suit.DIAMONDS), new Card(3, Suit.HEARTS)));
        assertEquals(HandRanking.STRAIGHT, RankingSeparator.calculateCards(cards));

        cards = new ArrayList<>();

        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(5, Suit.CLUBS)));
        assertEquals(HandRanking.STRAIGHT, RankingSeparator.calculateCards(cards));
    }

    @Test
    void testMountain() {
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(10, Suit.CLUBS),
                new Card(11, Suit.CLUBS), new Card(12, Suit.CLUBS), new Card(13, Suit.CLUBS)));
        assertEquals(HandRanking.MOUNTAIN, RankingSeparator.calculateCards(cards));
        cards.clear();
        cards.addAll(List.of(new Card(9, Suit.HEARTS), new Card(10, Suit.CLUBS),
                new Card(11, Suit.CLUBS), new Card(12, Suit.CLUBS), new Card(13, Suit.CLUBS)));
        assertNotEquals(HandRanking.MOUNTAIN, RankingSeparator.calculateCards(cards));
    }

    @Test
    void testFlush() {
        cards.addAll(List.of(new Card(1, Suit.CLUBS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(6, Suit.CLUBS)));
        assertEquals(HandRanking.FLUSH, RankingSeparator.calculateCards(cards));

        cards.addAll(List.of(new Card(10, Suit.CLUBS), new Card(11, Suit.CLUBS)));
        assertEquals(HandRanking.FLUSH, RankingSeparator.calculateCards(cards));
    }

    @Test
    void testFullHouse() {
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(1, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(4, Suit.CLUBS)));
        assertEquals(HandRanking.FULL_HOUSE, RankingSeparator.calculateCards(cards));
    }

    @Test
    void testFourCards() {
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(1, Suit.CLUBS), new Card(1, Suit.CLUBS), new Card(4, Suit.CLUBS)));
        assertEquals(HandRanking.FOUR_CARDS, RankingSeparator.calculateCards(cards));
    }

    @Test
    void testStraightFlush() {
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(2, Suit.DIAMONDS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(5, Suit.CLUBS)));
        cards.add(new Card(6, Suit.CLUBS));
        cards.add(new Card(7, Suit.CLUBS));
        assertEquals(HandRanking.STRAIGHT_FLUSH, RankingSeparator.calculateCards(cards));
    }

    @Test
    void testRoyalStraightFlush() {
        cards.addAll(List.of(new Card(1, Suit.CLUBS), new Card(11, Suit.CLUBS),
                new Card(10, Suit.CLUBS), new Card(12, Suit.CLUBS), new Card(13, Suit.CLUBS)));
        assertEquals(HandRanking.ROYAL_STRAIGHT_FLUSH, RankingSeparator.calculateCards(cards));

        cards.clear();
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(11, Suit.CLUBS),
                new Card(10, Suit.CLUBS), new Card(12, Suit.CLUBS), new Card(13, Suit.CLUBS),
                new Card(9, Suit.CLUBS)));
        assertEquals(HandRanking.STRAIGHT_FLUSH, RankingSeparator.calculateCards(cards));

        cards.clear();
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(11, Suit.CLUBS),
                new Card(10, Suit.CLUBS), new Card(12, Suit.CLUBS), new Card(13, Suit.CLUBS),
                new Card(8, Suit.CLUBS), new Card(9, Suit.DIAMONDS)));
        assertEquals(HandRanking.FLUSH, RankingSeparator.calculateCards(cards));
    }
}