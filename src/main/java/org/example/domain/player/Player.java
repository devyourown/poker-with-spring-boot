package org.example.domain.player;

import org.example.domain.card.Card;
import org.example.domain.rules.RankingCalculator;
import org.example.domain.rules.HandRanking;

import java.util.Collections;
import java.util.List;

public class Player {
    private String id;
    private int money;
    private List<Card> hands;
    private HandRanking handRanking;

    public Player(String id, int money) {
        this.id = id;
        this.money = money;
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

    public boolean isAllIn() {
        return money == 0;
    }

    public String getId() {
        return id;
    }

}
