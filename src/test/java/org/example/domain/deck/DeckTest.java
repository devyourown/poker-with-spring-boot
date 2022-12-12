package org.example.domain.deck;

import org.example.domain.card.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    @Test
    void testDeckHasCard() {
        Deck deck = new Deck(3);
        assertEquals(11, deck.getNumOfCards());
        assertEquals(Card.class, deck.draw().getClass());
        deck = new Deck(8);
        assertEquals(21, deck.getNumOfCards());
        deck = new Deck(4);
        assertEquals(13, deck.getNumOfCards());
    }
}