package org.example.domain.player;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;
import org.example.domain.error.BetException;
import org.example.domain.rules.HandRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;

    @BeforeEach
    void createPlayer() {
        player = new Player(1000);
    }

    @Test
    void testPlayerHasMoney() {
        assertEquals(1000, player.getMoney());
    }

    @Test
    void testPlayerCanBet() throws Exception {
        player.bet(500);
        assertEquals(500, player.getMoney());
    }

    @Test
    void testPlayerHasCards() {
        player.setHands(List.of(new Card(1, Suit.CLUBS), new Card(2, Suit.CLUBS)));
        assertEquals(2, player.getHands().size());
        assertEquals(Suit.CLUBS, player.getHands().get(0).getSuit());
        assertEquals(HandRanking.HIGH_CARD, player.getRanking());
        player.setHands(List.of(new Card(1, Suit.CLUBS), new Card(1, Suit.CLUBS)));
        assertEquals(HandRanking.ONE_PAIR, player.getRanking());
    }
}