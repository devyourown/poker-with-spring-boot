package org.example.domain.game;

import org.example.domain.player.Player;
import org.example.domain.rules.HandRanking;

import java.util.ArrayList;
import java.util.List;

public class Presenter {
    private Pot pot;

    public Presenter(Pot pot) {
        this.pot = pot;
    }

    public void giveMoneyToAllInWinner(List<Player> winner, List<Player> loser) {
        winner.sort((a, b) -> pot.getPlayerBetLog(a) - pot.getPlayerBetLog(b));
        for (int i=0; i<winner.size();) {
            giveWinnerMoney(winner,
                    makeAllInPrizeMoney(loser, pot.getPlayerBetLog(winner.get(i))));
            pot.takeOutMoney(pot.getPlayerBetLog(winner.get(i)));
            winner.remove(i);
        }
    }

    private void giveWinnerMoney(List<Player> winner, int prizeMoney) {
        for (Player player : winner) {
            player.raiseMoney(prizeMoney/winner.size()
                    + prizeMoney % winner.size());
        }
    }

    private int makeAllInPrizeMoney(List<Player> loser, int winnerBetSize) {
        int result = 0;
        for (Player player : loser) {
            result += getMoneyFromPot(player, winnerBetSize);
        }
        return result;
    }

    private int getMoneyFromPot(Player player, int winnerBetSize) {
        int playerBet = pot.getPlayerBetLog(player);
        if (playerBet > winnerBetSize) {
            getMoneyAndLogLeft(player, winnerBetSize, playerBet - winnerBetSize);
            return winnerBetSize;
        }
        getMoneyAndLogLeft(player, playerBet, 0);
        return playerBet;
    }

    private void getMoneyAndLogLeft(Player player, int money, int left) {
        pot.takeOutMoney(money);
        pot.putPlayerBetLog(player, left);
    }

    public void giveMoney(List<Player> winners) {
        if (isTiedGame(winners))
            giveWinnerMoney(winners, pot.getTotalAmount());
        else
            winners.get(0).raiseMoney(pot.getTotalAmount());
    }

    private boolean isTiedGame(List<Player> winners) {
        if (winners.size() > 1)
            return true;
        return false;
    }
}
