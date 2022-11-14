package org.example.domain.rules;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DealerTest {
    private List<Card> cards;
    @BeforeEach
    void createCards() {
        cards = new ArrayList<>();
    }
    @Test
    void testCalculateCorrectly() {

        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(5, Suit.CLUBS)));
        assertEquals(HandRanking.STRAIGHT, Dealer.calculateCards(cards));

        cards.addAll(List.of(new Card(1, Suit.CLUBS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(5, Suit.CLUBS)));
        assertEquals(HandRanking.FLUSH, Dealer.calculateCards(cards));

        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(1, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(4, Suit.CLUBS)));
        assertEquals(HandRanking.FULL_HOUSE, Dealer.calculateCards(cards));

        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(1, Suit.CLUBS), new Card(1, Suit.CLUBS), new Card(4, Suit.CLUBS)));
        assertEquals(HandRanking.FOUR_CARDS, Dealer.calculateCards(cards));

        cards.addAll(List.of(new Card(1, Suit.CLUBS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(5, Suit.CLUBS)));
        assertEquals(HandRanking.STRAIGHT_FLUSH, Dealer.calculateCards(cards));
    }

    @Test
    void testOnePair() {
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(1, Suit.CLUBS)));
        assertEquals(HandRanking.ONE_PAIR, Dealer.calculateCards(cards));
    }

    @Test
    void testTwoPair() {
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(2, Suit.CLUBS), new Card(1, Suit.CLUBS)));
        assertEquals(HandRanking.TWO_PAIR, Dealer.calculateCards(cards));
    }

    @Test
    void testTriple() {
        cards.addAll(List.of(new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(1, Suit.CLUBS)));
        assertEquals(HandRanking.TRIPLE, Dealer.calculateCards(cards));
    }

}