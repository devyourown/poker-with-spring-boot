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
            giveWinnerMoney(winner, makeAllInPrizeMoney(loser,
                    pot.getPlayerBetLog(winner.get(i))));
            pot.takeOutMoney(pot.getPlayerBetLog(winner.get(i)));
            winner.remove(i);
        }
    }

    private int makeAllInPrizeMoney(List<Player> loser, int winnerBetSize) {
        int result = 0;
        for (Player player : loser) {
            int playerBet = pot.getPlayerBetLog(player);
            if (playerBet > winnerBetSize) {
                pot.putPlayerBetLog(player, playerBet - winnerBetSize);
                pot.takeOutMoney(winnerBetSize);
                result += winnerBetSize;
            } else {
                result += playerBet;
                pot.takeOutMoney(playerBet);
                pot.putPlayerBetLog(player, 0);
            }
        }
        return result;
    }

    private void giveWinnerMoney(List<Player> winner, int prizeMoney) {
        for (Player player : winner) {
            player.raiseMoney(prizeMoney/winner.size()
                    + prizeMoney % winner.size());
        }
    }

    public void giveMoney(List<Player> winners) {
        if (isTiedGame(winners)) {
            for (Player player : winners)
                player.raiseMoney(getWinnerPrize(winners));
        }
        else
            winners.get(0).raiseMoney(pot.getTotalAmount());
    }

    private boolean isTiedGame(List<Player> winners) {
        if (winners.size() > 1)
            return true;
        return false;
    }

    private int getWinnerPrize(List<Player> winners) {
        return pot.getTotalAmount() / winners.size() +
                pot.getTotalAmount() % winners.size();
    }
}
