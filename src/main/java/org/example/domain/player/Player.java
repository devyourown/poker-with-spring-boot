package org.example.domain.player;

import org.example.domain.card.Card;
import org.example.domain.error.BetException;
import org.example.domain.rules.RankingSeperator;
import org.example.domain.rules.HandRanking;

import java.util.List;

public class Player {
    private int money;
    private List<Card> hands;
    private HandRanking handRanking;

    public Player(int money) {
        this.money = money;
    }

    public void bet(int betAmount) throws Exception {
        validateEnoughToBet(betAmount);
        this.money -= betAmount;
    }

    private void validateEnoughToBet(int amount) throws Exception {
        if (money < amount)
            throw new BetException(BetException.ErrorCode.MONEY_NOT_ENOUGH);
    }

    public int getMoney() {
        return money;
    }

    public void raiseMoney(int pot) {
        this.money += pot;
    }

    public void setHands(List<Card> cards) {
        hands = cards;
        handRanking = RankingSeperator.calculateHands(cards);
    }

    public List<Card> getHands() {
        return hands;
    }

    public HandRanking getRanking() {
        return handRanking;
    }

    public void setHandRanking(HandRanking handRanking) {
        this.handRanking = handRanking;
    }
}
