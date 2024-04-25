package org.poker.domain.player;

import org.poker.domain.card.Card;

import java.util.Collections;
import java.util.List;

public class Player {
    private final String id;
    private int money;
    private List<Card> hands;
    private long ranks;
    private int possibleTakingAmountOfMoney;
    private int beforeBetMoney;
    private int betSize;

    public Player(String id, int money) {
        this.id = id;
        this.money = money;
        this.beforeBetMoney = money;
        this.ranks = 0;
        this.possibleTakingAmountOfMoney = 0;
        this.betSize = 0;
    }

    public void gameOver() {
        beforeBetMoney = money;
        possibleTakingAmountOfMoney = 0;
        betSize = 0;
    }

    public int getBeforeBetMoney() {
        return beforeBetMoney;
    }

    public void bet(int betAmount) {
        this.money -= betAmount;
        betSize += betAmount;
    }

    public int getMoney() {
        return money;
    }

    public void raiseMoney(int pot) {
        this.money += pot;
    }

    public void setHands(List<Card> cards) {
        hands = cards;
    }

    public List<Card> getHands() {
        return Collections.unmodifiableList(hands);
    }

    public String getId() {
        return id;
    }

    public void plusPossibleTakingAmountOfMoney(int money) {
        this.possibleTakingAmountOfMoney += money;
    }

    public int getPossibleTakingAmountOfMoney() {
        return this.possibleTakingAmountOfMoney;
    }

    public int getBetSize() { return this.betSize; }

    public boolean hasAllin() {
        return money == 0;
    }

    public void setRanks(long ranks) {
        this.ranks = ranks;
    }

    public long getRanks() {
        return this.ranks;
    }

}
