package org.example.domain.rules;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DealerTest {
    @Test
    void testCalculateCorrectly() {
        List<Card> cards = new ArrayList<>();
        cards.addAll(List.of(new Card(1, Suit.CLUBS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(1, Suit.CLUBS)));
        assertEquals(HandRanking.ONE_PAIR, Dealer.calculateCards(cards));
        cards.addAll(List.of(new Card(1, Suit.CLUBS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(2, Suit.CLUBS), new Card(1, Suit.CLUBS)));
        assertEquals(HandRanking.TWO_PAIR, Dealer.calculateCards(cards));
        cards.addAll(List.of(new Card(1, Suit.CLUBS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(1, Suit.CLUBS)));
        assertEquals(HandRanking.TRIPLE, Dealer.calculateCards(cards));
        cards.addAll(List.of(new Card(1, Suit.CLUBS), new Card(2, Suit.CLUBS),
                new Card(3, Suit.CLUBS), new Card(4, Suit.CLUBS), new Card(1, Suit.CLUBS)));
        assertEquals(HandRanking.ONE_PAIR, Dealer.calculateCards(cards));
    }
}