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
            playerBetLog.remove(player);
            player.giveUpMoney();
        }
        calculatePossibleMoney();
        playerBetLog.clear();
        this.currentBet = 0;
        this.turnAmount = 0;
    }

    private void calculatePossibleMoney() {
        for (Map.Entry<Player, Integer> entry : playerBetLog.entrySet()) {
            int canTakeMoney = entry.getValue() * playerBetLog.size();
            Player player = entry.getKey();
            if (canTakeMoney > turnAmount)
                player.plusPossibleTakingAmountOfMoney(turnAmount);
            else
                player.plusPossibleTakingAmountOfMoney(canTakeMoney);
        }
    }

    public void splitMoney(List<Player> winners, List<Player> losers) {
        winners.sort(Comparator.comparingInt(Player::getPossibleTakingAmountOfMoney).reversed());
        getMoneyTogether(new LinkedList<>(winners));
        if (getTotalAmount() >= 100 && losers.size() >= 1) {
            List<Player> chopped = getChopped(losers);
            chopped.sort(Comparator.comparingInt(Player::getPossibleTakingAmountOfMoney));
            int split = getTotalAmount() / chopped.size();
             for (Player player : chopped) {
                int money = player.getPossibleTakingAmountOfMoney();
                if (money < split)
                    money = split;
                int possibleMoney = takeOutMoney(money);
                player.raiseMoney(possibleMoney);
            }
        }
    }

    private List<Player> getChopped(List<Player> players) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.getRanks() == players.get(0).getRanks())
                result.add(player);
            else
                break;
        }
        return result;
    }

    private void getMoneyTogether(Queue<Player> players) {
        if (players.size() > 1) {
            int totalMoney = players.stream()
                    .mapToInt(Player::getPossibleTakingAmountOfMoney)
                    .reduce(Integer::sum).orElse(0);
            for (Player player : players) {
                if (totalMoney / players.size() == getTotalAmount()) {
                    for (Player allPlayer : players) {
                        allPlayer.raiseMoney(totalMoney/players.size());
                    }
                    takeOutMoney(totalMoney);
                    return;
                } else {
                    player.raiseMoney(player.getPossibleTakingAmountOfMoney());
                    takeOutMoney(player.getPossibleTakingAmountOfMoney());
                    players.poll();
                }
            }
        } else {
            Player player = players.poll();
            int money = player.getPossibleTakingAmountOfMoney();
            int possibleMoney = takeOutMoney(money);
            System.out.println("Money : " + money +" possible : " + possibleMoney);
            player.raiseMoney(possibleMoney);
        }
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
