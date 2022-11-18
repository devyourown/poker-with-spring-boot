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
    void testBigAllIn() {
        players = new ArrayList<>();
        player = new Player(3000);
        player.setHandRanking(HandRanking.FLUSH);
        players.add(player);

        player2 = new Player(4000);
        player2.setHandRanking(HandRanking.FLUSH);
        players.add(player2);

        player3 = new Player(7000);
        player3.setHandRanking(HandRanking.ONE_PAIR);
        players.add(player3);

        Player player4 = new Player(8000);
        player4.setHandRanking(HandRanking.STRAIGHT);
        players.add(player4);

        Player player5 = new Player(1500);
        player5.setHandRanking(HandRanking.HIGH_CARD);
        players.add(player5);

        Pot pot = new Pot(players, 100, 200);

        pot.putPlayerBetLog(player, 3000);
        player.bet(3000);
        pot.raiseMoney(3000);

        pot.putPlayerBetLog(player2, 4000);
        player2.bet(4000);
        pot.raiseMoney(4000);

        pot.putPlayerBetLog(player3, 7000);
        player3.bet(7000);
        pot.raiseMoney(7000);

        pot.putPlayerBetLog(player4, 8000);
        player4.bet(7900);
        pot.raiseMoney(7900);

        pot.putPlayerBetLog(player5, 1500);
        player5.bet(1300);
        pot.raiseMoney(1300);

        gameResult = new GameResult(players, pot);

        assertEquals(6750, player.getMoney());
        assertEquals(9750, player2.getMoney());
        assertEquals(0, player3.getMoney());
        assertEquals(7000, player4.getMoney());
        assertEquals(0, player5.getMoney());
        assertEquals(0, pot.getTotalAmount());
    }

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
        pot.raiseMoney(1000);

        pot.putPlayerBetLog(player2, 1000);
        player2.bet(900);
        pot.raiseMoney(900);

        pot.putPlayerBetLog(player3, 1000);
        player3.bet(800);
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
        pot.raiseMoney(1000);

        pot.putPlayerBetLog(player2, 500);
        player2.bet(400);
        pot.raiseMoney(400);

        pot.putPlayerBetLog(player3, 1000);
        player3.bet(800);
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
        pot.raiseMoney(1000);

        pot.putPlayerBetLog(player2, 500);
        player2.bet(400);
        pot.raiseMoney(400);

        pot.putPlayerBetLog(player3, 1000);
        player3.bet(800);
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
        pot.raiseMoney(1000);

        pot.putPlayerBetLog(player2, 500);
        player2.bet(400);
        pot.raiseMoney(400);

        pot.putPlayerBetLog(player3, 1000);
        player3.bet(800);
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