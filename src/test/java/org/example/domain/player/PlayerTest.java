package org.example.domain.player;

import org.poker.domain.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}