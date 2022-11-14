package org.example.domain.player;

import org.example.domain.card.Card;
import org.example.domain.rules.Dealer;
import org.example.domain.rules.HandRanking;

import java.util.List;

public class Player {
    private int money;
    private List<Card> hands;
    private HandRanking handRanking;

    public Player(int money) {
        this.money = money;
    }

    public void setHands(List<Card> cards) {
        hands = cards;
        handRanking = Dealer.calculateHands(cards);
    }

    public List<Card> getHands() {
        return hands;
    }

    public HandRanking getRanking() {
        return handRanking;
    }

    public void bet(int betAmount) throws Exception {
        validateEnoughToBet(betAmount);
        this.money -= betAmount;
    }

    private void validateEnoughToBet(int amount) throws Exception {
        if (money < amount)
            throw new IllegalArgumentException("[ERROR] 베팅할 돈이 부족합니다.");
    }

    public int getMoney() {
        return money;
    }
}
