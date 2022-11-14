package org.example.domain;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    @Test
    void testCardHasValue() {
        assertEquals(1, new Card(1, Suit.HEARTS).getValue());
        assertEquals(Suit.HEARTS, new Card(1, Suit.HEARTS).getSuit());
    }

    @Test
    void testCardCanBeCompared() {
        assertTrue(new Card(1, Suit.HEARTS).equals(new Card(1, Suit.HEARTS)));
        assertFalse(new Card(1, Suit.HEARTS).equals(new Card(1, Suit.HEARTS)));
    }

}