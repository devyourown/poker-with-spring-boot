package org.example.domain.game.helper;

import org.example.domain.error.BetException;
import org.example.domain.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pot {
    private Map<String, Integer> playerBetLog;
    private int smallBlind;
    private int bigBlind;
    private int currentBet;
    private int totalAmount;

    public Pot(List<Player> players, int smallBlind, int bigBlind) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        initPlayerBetLog(players);
    }

    private void initPlayerBetLog(List<Player> players) {
        this.currentBet = bigBlind;
        this.totalAmount = smallBlind + bigBlind;
        playerBetLog = new HashMap<>();
        for (Player player : players)
            playerBetLog.put(player.getId(), 0);
        players.get(players.size() - 2).bet(smallBlind);
        players.get(players.size() - 1).bet(bigBlind);
        playerBetLog.put(players.get(players.size() - 2).getId(), smallBlind);
        playerBetLog.put(players.get(players.size() - 1).getId(), bigBlind);
    }

    public void reset() {
        playerBetLog.clear();
        this.currentBet = bigBlind;
    }

    private void splitMoney(List<Player> winner, List<Player> loser) {

    }

    public void call(Player player) {
        int callAmount =  currentBet - playerBetLog.get(player.getId());
        player.bet(callAmount);
        raiseMoney(callAmount);
    }

    private void raiseMoney(int money) {
        this.totalAmount += money;
    }

    public void bet(Player player, int betSize) {
        this.currentBet = betSize;
        player.bet(betSize);
        raiseMoney(betSize);
        playerBetLog.put(player.getId(), betSize);
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getPlayerBetLog(Player player) {
        return playerBetLog.get(player.getId());
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void takeOutMoney(int money) {
        this.totalAmount -= money;
    }

    public void returnMoneyTo(Player player) {
        int moneySize = getPlayerBetLog(player);
        this.totalAmount -= moneySize;
        player.raiseMoney(moneySize);
    }
}
