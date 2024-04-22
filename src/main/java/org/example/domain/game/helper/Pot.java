package org.example.domain.game.helper;

import org.example.domain.error.BetException;
import org.example.domain.player.Player;
import org.springframework.security.core.parameters.P;

import java.util.*;
import java.util.stream.Collectors;

public class Pot {
    private Map<Player, Integer> playerBetLog;
    private final int smallBlind;
    private final int bigBlind;
    private int currentBet;
    private int totalAmount;
    private int turnBet;

    public Pot(List<Player> players, int smallBlind, int bigBlind) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        initPlayerBetLog(players);
    }

    private void initPlayerBetLog(List<Player> players) {
        this.currentBet = bigBlind;
        this.totalAmount = smallBlind + bigBlind;
        this.turnBet = totalAmount;
        playerBetLog = new HashMap<>();
        for (Player player : players)
            playerBetLog.put(player, 0);
        players.get(players.size() - 2).bet(smallBlind);
        players.get(players.size() - 1).bet(bigBlind);
        playerBetLog.put(players.get(players.size() - 2), smallBlind);
        playerBetLog.put(players.get(players.size() - 1), bigBlind);
    }

    public void refresh(List<Player> foldPlayers) {
        for (Player player : foldPlayers) {
            playerBetLog.remove(player);
            player.giveUpMoney();
        }
        calculatePossibleMoney();
        playerBetLog.clear();
        this.currentBet = bigBlind;
        this.turnBet = 0;
    }

    private void calculatePossibleMoney() {
        int amountOfAllinPlayer = 0;
        List<Map.Entry<Player, Integer>> sorted =
                playerBetLog.entrySet().stream().sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toList());
        for (Map.Entry<Player, Integer> entry : sorted) {
            int canTakeMoney = entry.getValue() * playerBetLog.size();
            Player player = entry.getKey();
            if (canTakeMoney > this.turnBet) {
                player.plusPossibleTakingAmountOfMoney(this.turnBet);
            } else {
                player.plusPossibleTakingAmountOfMoney(canTakeMoney + amountOfAllinPlayer);
                playerBetLog.remove(player);
                amountOfAllinPlayer += entry.getValue();
            }
        }
    }

    public void splitMoney(List<Player> winners, List<Player> losers) {
        winners.sort(Comparator.comparingInt(Player::getPossibleTakingAmountOfMoney).reversed());
        if (canGetMoneyOneAnother(winners))
            getMoneyOneAnother(winners);
        else
            getMoneyTogether(winners);
        if (getTotalAmount() >= 100) {
            List<Player> chopped = getChopped(losers);
            chopped.sort(Comparator.comparingInt(Player::getPossibleTakingAmountOfMoney));
            int splitted = getTotalAmount() / chopped.size();
            for (Player player : chopped) {
                int money = player.getPossibleTakingAmountOfMoney();
                if (money < splitted)
                    money = splitted;
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

    private boolean canGetMoneyOneAnother(List<Player> players) {
        if (players.size() > 1)
            return players.stream().filter(player -> players.get(0).getPossibleTakingAmountOfMoney()
                    != player.getPossibleTakingAmountOfMoney()).count() > 1;
        return false;
    }

    private void getMoneyTogether(List<Player> players) {
        if (players.size() > 1) {
            for (Player player : players) {
                int money = player.getPossibleTakingAmountOfMoney() / players.size();
                int possibleMoney = takeOutMoney(money);
                player.raiseMoney(possibleMoney);
            }
        } else {
            int money = players.get(0).getPossibleTakingAmountOfMoney();
            int possibleMoney = takeOutMoney(money);
            players.get(0).raiseMoney(possibleMoney);
        }
    }

    private void getMoneyOneAnother(List<Player> players) {
        players.sort(Comparator.comparingInt(Player::getPossibleTakingAmountOfMoney));
        for (Player player : players) {
            int money = player.getPossibleTakingAmountOfMoney();
            int possibleMoney = takeOutMoney(money);
            player.raiseMoney(possibleMoney);
        }
    }

    public void call(Player player) {
        int callAmount =  currentBet - playerBetLog.get(player);
        player.bet(callAmount);
        raiseMoney(callAmount);
    }

    private void raiseMoney(int money) {
        this.totalAmount += money;
        this.turnBet += money;
    }

    public void bet(Player player, int betSize) {
        this.currentBet = betSize;
        player.bet(betSize);
        raiseMoney(betSize);
        playerBetLog.put(player, betSize);
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    private int takeOutMoney(int money) {
        if (money <= totalAmount) {
            totalAmount -= money;
            return totalAmount;
        }
        money = totalAmount;
        totalAmount = 0;
        return money;
    }
}
