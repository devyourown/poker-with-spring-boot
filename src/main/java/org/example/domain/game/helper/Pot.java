package org.example.domain.game.helper;

import org.example.domain.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pot {
    private Map<Player, Integer> playerBetLog;
    private int smallBlind;
    private int bigBlind;
    private int currentBet;
    private int totalAmount;

    public Pot(List<Player> players, int smallBlind, int bigBlind) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.currentBet = bigBlind;
        this.totalAmount = smallBlind + bigBlind;
        initPlayerBetLog(players);
    }

    private void initPlayerBetLog(List<Player> players) {
        playerBetLog = new HashMap<>();
        for (int i = 0; i < players.size() - 2; i++)
            playerBetLog.put(players.get(i), 0);
        players.get(players.size() - 2).bet(smallBlind);
        players.get(players.size() - 1).bet(bigBlind);
        playerBetLog.put(players.get(players.size() - 2), smallBlind);
        playerBetLog.put(players.get(players.size() - 1), bigBlind);
    }

    public void reset(List<Player> players) {
        this.currentBet = bigBlind;
        this.totalAmount = smallBlind + bigBlind;
        initPlayerBetLog(players);
    }

    public void putZeroInBetLog(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            playerBetLog.put(players.get(i), 0);
        }
    }

    public int amountToCall(Player player) {
        return currentBet - playerBetLog.get(player);
    }

    public void raiseMoney(int money) {
        this.totalAmount += money;
    }

    public void setCurrentBet(int betSize) {
        this.currentBet = betSize;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void putPlayerBetLog(Player player, int betSize) {
        playerBetLog.put(player, betSize);
    }

    public int getPlayerBetLog(Player player) {
        return playerBetLog.get(player);
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void resetCurrentBet() {
        currentBet = 0;
    }

    public void takeOutMoney(int money) {
        this.totalAmount -= money;
    }
}
