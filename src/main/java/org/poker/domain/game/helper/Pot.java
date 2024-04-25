package org.poker.domain.game.helper;

import org.poker.domain.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Pot {
    private final Map<Player, Integer> playerBetLog;
    private final int smallBlind;
    private final int bigBlind;
    private int currentBet;
    private int totalAmount;
    private int turnAmount;
    private int foldAmount;

    public Pot(List<Player> players, int smallBlind, int bigBlind) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        playerBetLog = new HashMap<>();
        reset(players);
    }

    public void reset(List<Player> players) {
        this.currentBet = bigBlind;
        this.totalAmount = smallBlind + bigBlind;
        this.turnAmount = smallBlind + bigBlind;
        this.foldAmount = 0;
        paySmallBig(players.get(players.size()-2), players.get(players.size()-1));
    }

    private void paySmallBig(Player smallBlinder, Player bigBlinder) {
        smallBlinder.bet(smallBlind);
        bigBlinder.bet(bigBlind);
        playerBetLog.put(smallBlinder, smallBlind);
        playerBetLog.put(bigBlinder, bigBlind);
    }

    public void refresh(List<Player> foldPlayers) {
        for (Player player : foldPlayers) {
            foldAmount += playerBetLog.getOrDefault(player, 0);
            playerBetLog.remove(player);
            player.gameOver();
        }
        calculatePossibleMoney();
        playerBetLog.clear();
        this.currentBet = 0;
        this.turnAmount = 0;
        this.foldAmount = 0;
    }

    private void calculatePossibleMoney() {
        for (Map.Entry<Player, Integer> entry : playerBetLog.entrySet()) {
            int canTakeMoney = entry.getValue() * playerBetLog.size() + foldAmount;
            Player player = entry.getKey();
            player.plusPossibleTakingAmountOfMoney(Math.min(turnAmount, canTakeMoney));
        }
    }

    public void splitMoney(List<Player> winners, List<Player> losers) {
        winners.sort(Comparator.comparingInt(Player::getPossibleTakingAmountOfMoney));
        giveMoneyTo(winners);
        if (hasMoneyLeftOver() && losers.size() > 0) {
            List<Player> chopped = getChopped(losers);
            int bestBet = winners.stream().mapToInt(Player::getBeforeBetMoney)
                    .max().orElse(0);
            payBack(chopped, bestBet);
            if (hasMoneyLeftOver())
                 splitLeftOver(chopped);
        }
    }

    private void splitLeftOver(List<Player> losers) {
        int split = getTotalAmount() / losers.size();
        losers.forEach(player -> player.raiseMoney(split));
        takeOutMoney(getTotalAmount());
    }

    private void payBack(List<Player> players, int alreadyTaken) {
        for (Player player : players)
            player.raiseMoney(takeOutMoney(player.getBetSize() - alreadyTaken));
    }

    private boolean hasMoneyLeftOver() {
        return getTotalAmount() > 0;
    }

    private List<Player> getChopped(List<Player> players) {
        return players.stream()
                .filter(player -> player.getRanks() ==
                        players.get(0).getRanks())
                .sorted(Comparator.comparingLong(Player::getBeforeBetMoney))
                .collect(Collectors.toList());
    }

    private void giveMoneyTo(List<Player> players) {
        if (players.size() > 1) {
            payBack(players, 0);
            takeMoneyWhenChopped(players);
            return;
        }
        takeMoneyOnePerson(Objects.requireNonNull(players.get(0)));
    }

    private void takeMoneyWhenChopped(List<Player> players) {
        int size = players.size();
        for (Player player : players) {
            int possibleMoney = player.getBetSize()/size;
            if (possibleMoney > getTotalAmount()/size)
                possibleMoney = getTotalAmount()/size;
            player.raiseMoney(takeOutMoney(possibleMoney));
            size -= 1;
        }
    }

    private void takeMoneyOnePerson(Player player) {
        int money = player.getPossibleTakingAmountOfMoney();
        int possibleMoney = takeOutMoney(money);
        player.raiseMoney(possibleMoney);
    }

    public void call(Player player) {
        int callAmount = getSizeToCall(player);
        player.bet(callAmount);
        raiseMoney(callAmount);
        playerBetLog.put(player, currentBet);
    }

    private void raiseMoney(int money) {
        this.totalAmount += money;
        this.turnAmount += money;
    }

    public int getSizeToCall(Player player) {
        int alreadyPayed = 0;
        if (playerBetLog.containsKey(player))
            alreadyPayed = playerBetLog.get(player);
        return currentBet - alreadyPayed;
    }

    public void bet(Player player, int betSize) {
        player.bet(betSize);
        raiseMoney(betSize);
        if (playerBetLog.containsKey(player))
            betSize += playerBetLog.get(player);
        this.currentBet = betSize;
        playerBetLog.put(player, betSize);
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public int getBigBlind() { return bigBlind; }

    private int takeOutMoney(int money) {
        if (money <= totalAmount) {
            totalAmount -= money;
            return money;
        }
        money = totalAmount;
        totalAmount = 0;
        return money;
    }
}
