package org.example.domain.game.helper;

import org.example.domain.player.Player;

import java.util.List;

public class Presenter {
    private Pot pot;

    public Presenter(Pot pot) {
        this.pot = pot;
    }

    /* 진 사람이 올인 싸움에서 돈을 더 많이 냈을 경우를 따진다.*/
    public void giveMoneyToAllInWinner(List<Player> winner, List<Player> loser) {
        winner.sort((a, b) -> pot.getPlayerBetLog(a) - pot.getPlayerBetLog(b));
        returnMoneyFromBetting(winner);
        for (int i=0; i<winner.size();) {
            int winnerBet = pot.getPlayerBetLog(winner.get(i));
            giveWinnerMoney(winner, makeAllInPrizeMoney(loser, winnerBet));
            reduceMoneyAfterGetting(winner, winnerBet);
        }
    }

    private void returnMoneyFromBetting(List<Player> players) {
        for (Player player : players) {
            pot.returnMoneyTo(player);
        }
    }

    private void giveWinnerMoney(List<Player> winner, int prizeMoney) {
        for (Player player : winner) {
            player.raiseMoney(prizeMoney / winner.size()
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

    private void reduceMoneyAfterGetting(List<Player> winner, int reduceAmount) {
        winner.remove(0);
        for (Player player : winner) {
            pot.putPlayerBetLog(player, pot.getPlayerBetLog(player) - reduceAmount);
        }
    }

    public void giveMoney(List<Player> winners) {
        if (isTiedGame(winners))
            giveWinnerMoney(winners, pot.getTotalAmount());
        else
            winners.get(0).raiseMoney(pot.getTotalAmount());
        pot.takeOutMoney(pot.getTotalAmount());
    }

    private boolean isTiedGame(List<Player> winners) {
        if (winners.size() > 1)
            return true;
        return false;
    }
}
