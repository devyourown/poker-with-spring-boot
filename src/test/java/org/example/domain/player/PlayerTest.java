package org.example.domain.player;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;
import org.example.domain.rules.Ranking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void createPlayer() {
        player = new Player("1", 1000);
    }

    @Test
    void testPlayerHasMoney() {
        assertEquals(1000, player.getMoney());
    }

    @Test
    void testPlayerCanBet() {
        player.bet(500);
        assertEquals(500, player.getMoney());
    }

    @Test
    void testPlayerHasCards() {
        player.setHands(List.of(Card.of(1, Suit.CLUBS), Card.of(2, Suit.CLUBS)));
        assertEquals(2, player.getHands().size());
        assertEquals(Suit.CLUBS, player.getHands().get(0).getSuit());
        assertEquals(Ranking.HIGH_CARD, player.getRanking());
        player.setHands(List.of(Card.of(1, Suit.CLUBS), Card.of(1, Suit.CLUBS)));
        assertEquals(Ranking.ONE_PAIR, player.getRanking());
    }
}