package org.example.domain.deck;

import org.example.domain.card.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    @Test
    void testDeckHasCard() {
        Deck deck = new Deck();
        assertEquals(52, deck.getNumOfCards());
        assertEquals(Card.class, deck.draw().getClass());
    }
}