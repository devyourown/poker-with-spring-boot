package org.example.domain.card;

import org.poker.domain.card.Card;
import org.poker.domain.card.Suit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    @Test
    void testCardHasValue() {
        assertEquals(1, Card.of(1, Suit.HEARTS).getValue());
        assertEquals(Suit.HEARTS, Card.of(1, Suit.HEARTS).getSuit());
    }

    @Test
    void testCardCanBeCompared() {
        assertTrue(Card.of(1, Suit.HEARTS).isSameValue(Card.of(1, Suit.HEARTS)));
        assertTrue(Card.of(1, Suit.HEARTS).isSameValue(Card.of(1, Suit.CLUBS)));
        assertTrue(Card.of(3, Suit.HEARTS).isHigherThan(Card.of(2, Suit.CLUBS)));
        assertTrue(Card.of(1, Suit.HEARTS).isHigherThan(Card.of(2, Suit.HEARTS)));
    }

}