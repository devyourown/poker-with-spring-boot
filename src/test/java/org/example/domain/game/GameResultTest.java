package org.example.domain.game;

import org.example.domain.player.Player;
import org.example.domain.rules.HandRanking;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameResultTest {
    @Test
    void testAllinCase() {
        List<Player> players = new ArrayList<>();
        Player player = new Player(1000);
        player.setHandRanking(HandRanking.FLUSH);
        players.add(player);

        Player player2 = new Player(1000);
        player2.setHandRanking(HandRanking.FLUSH);
        players.add(player2);

        Player player3 = new Player(1000);
        player3.setHandRanking(HandRanking.FLUSH);
        players.add(player3);

        GameResult gameResult = new GameResult(players, new Pot(players, 100, 200));
    }
}