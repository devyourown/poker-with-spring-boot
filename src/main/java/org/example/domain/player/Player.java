package org.example.domain.player;

import org.example.domain.card.Card;
import org.example.domain.error.BetException;
import org.example.domain.rules.RankingCalculator;
import org.example.domain.rules.HandRanking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private int money;
    private List<Card> hands;
    private HandRanking handRanking;
    private boolean allIn;

    public Player(int money) {
        this.money = money;
        this.allIn = false;
    }

    public void bet(int betAmount) {
        this.money -= betAmount;
    }

    public int getMoney() {
        return money;
    }

    public void raiseMoney(int pot) {
        this.money += pot;
    }

    public void setHands(List<Card> cards) {
        hands = cards;
        handRanking = RankingCalculator.calculateHands(cards);
    }

    public List<Card> getHands() {
        return Collections.unmodifiableList(hands);
    }

    public HandRanking getRanking() {
        return handRanking;
    }

    public void setHandRanking(HandRanking handRanking) {
        this.handRanking = handRanking;
    }

    public void setAllIn() {
        allIn = true;
    }

    public boolean isAllIn() {
        return allIn;
    }

}
