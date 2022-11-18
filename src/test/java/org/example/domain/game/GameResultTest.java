package org.example.domain.game;

import org.example.domain.player.Player;
import org.example.domain.rules.HandRanking;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameResultTest {

    private List<Player> players;
    private Player player;
    private Player player2;
    private Player player3;
    private GameResult gameResult;

    @Test
    void testAllInTieWin() {
        List<Player> players = new ArrayList<>();
        Player player = new Player(1000);
        player.setHandRanking(HandRanking.FLUSH);
        players.add(player);

        Player player2 = new Player(1000);
        player2.setHandRanking(HandRanking.FLUSH);
        players.add(player2);

        Player player3 = new Player(1000);
        player3.setHandRanking(HandRanking.ONE_PAIR);
        players.add(player3);

        Pot pot = new Pot(players, 100, 200);

        pot.putPlayerBetLog(player, 1000);
        player.bet(1000);
        player.setAllIn();
        pot.raiseMoney(1000);

        pot.putPlayerBetLog(player2, 1000);
        player2.bet(900);
        player2.setAllIn();
        pot.raiseMoney(900);

        pot.putPlayerBetLog(player3, 1000);
        player3.bet(800);
        player3.setAllIn();
        pot.raiseMoney(800);

        gameResult = new GameResult(players, pot);

        assertEquals(1500, player.getMoney());
        assertEquals(1500, player2.getMoney());
    }

    @Test
    void testAllinTieWinDiffAmount() {
        players = new ArrayList<>();
        player = new Player(1000);
        player.setHandRanking(HandRanking.FLUSH);
        players.add(player);

        player2 = new Player(500);
        player2.setHandRanking(HandRanking.FLUSH);
        players.add(player2);

        player3 = new Player(1000);
        player3.setHandRanking(HandRanking.ONE_PAIR);
        players.add(player3);

        Pot pot = new Pot(players, 100, 200);

        pot.putPlayerBetLog(player, 1000);
        player.bet(1000);
        player.setAllIn();
        pot.raiseMoney(1000);

        pot.putPlayerBetLog(player2, 500);
        player2.bet(400);
        player2.setAllIn();
        pot.raiseMoney(400);

        pot.putPlayerBetLog(player3, 1000);
        player3.bet(800);
        player3.setAllIn();
        pot.raiseMoney(800);

        gameResult = new GameResult(players, pot);

        assertEquals(1750, player.getMoney());
        assertEquals(750, player2.getMoney());
    }

    @Test
    void testAllinOneWinDiffAmount() {
        players = new ArrayList<>();
        player = new Player(1000);
        player.setHandRanking(HandRanking.FLUSH);
        players.add(player);

        player2 = new Player(500);
        player2.setHandRanking(HandRanking.TWO_PAIR);
        players.add(player2);

        player3 = new Player(1000);
        player3.setHandRanking(HandRanking.ONE_PAIR);
        players.add(player3);

        Pot pot = new Pot(players, 100, 200);

        pot.putPlayerBetLog(player, 1000);
        player.bet(1000);
        player.setAllIn();
        pot.raiseMoney(1000);

        pot.putPlayerBetLog(player2, 500);
        player2.bet(400);
        player2.setAllIn();
        pot.raiseMoney(400);

        pot.putPlayerBetLog(player3, 1000);
        player3.bet(800);
        player3.setAllIn();
        pot.raiseMoney(800);

        gameResult = new GameResult(players, pot);

        assertEquals(2500, player.getMoney());
        assertEquals(0, player2.getMoney());
    }

    @Test
    void testAllinLowAmountWin() {
        players = new ArrayList<>();
        player = new Player(1000);
        player.setHandRanking(HandRanking.TWO_PAIR);
        players.add(player);

        player2 = new Player(500);
        player2.setHandRanking(HandRanking.FLUSH);
        players.add(player2);

        player3 = new Player(1000);
        player3.setHandRanking(HandRanking.ONE_PAIR);
        players.add(player3);

        Pot pot = new Pot(players, 100, 200);
        pot.putPlayerBetLog(player, 1000);
        player.bet(1000);
        player.setAllIn();
        pot.raiseMoney(1000);
        pot.putPlayerBetLog(player2, 500);
        player2.bet(400);
        player2.setAllIn();
        pot.raiseMoney(400);
        pot.putPlayerBetLog(player3, 1000);
        player3.bet(800);
        player3.setAllIn();
        pot.raiseMoney(800);
        gameResult = new GameResult(players, pot);
        assertEquals(1000, player.getMoney());
        assertEquals(1500, player2.getMoney());
        assertEquals(0, player3.getMoney());
    }

    @Test
    void testAllinCaseOneWin() {
        players = new ArrayList<>();
        player = new Player(1000);
        player.setHandRanking(HandRanking.FLUSH);
        players.add(player);

        player2 = new Player(1000);
        player2.setHandRanking(HandRanking.HIGH_CARD);
        players.add(player2);

        player3 = new Player(1000);
        player3.setHandRanking(HandRanking.ONE_PAIR);
        players.add(player3);

        Pot pot = new Pot(players, 100, 200);
        pot.putPlayerBetLog(player, 1000);
        player.bet(1000);
        pot.raiseMoney(1000);
        pot.putPlayerBetLog(player2, 1000);
        player2.bet(900);
        pot.raiseMoney(900);
        pot.putPlayerBetLog(player3, 1000);
        player3.bet(800);
        pot.raiseMoney(800);
        gameResult = new GameResult(players, pot);
        assertEquals(3000, player.getMoney());
    }
}