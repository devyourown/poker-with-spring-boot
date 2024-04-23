package org.example.domain.player;

import org.example.domain.card.Card;

import java.util.Collections;
import java.util.List;

public class Player {
    private final String id;
    private int money;
    private List<Card> hands;
    private long ranks;
    private RoomStatus roomStatus;
    private int possibleTakingAmountOfMoney;

    public Player(String id, int money) {
        this.id = id;
        this.money = money;
        this.roomStatus = RoomStatus.WAITING;
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
    }

    public List<Card> getHands() {
        return Collections.unmodifiableList(hands);
    }

    public String getId() {
        return id;
    }

    public boolean isRoomReady() {
        return this.roomStatus == RoomStatus.READY;
    }

    public void changeRoomStatus() {
        this.roomStatus = this.roomStatus == RoomStatus.WAITING ? RoomStatus.READY : RoomStatus.WAITING;
    }

    public void plusPossibleTakingAmountOfMoney(int money) {
        this.possibleTakingAmountOfMoney += money;
    }

    public void giveUpMoney() {
        this.possibleTakingAmountOfMoney = 0;
    }

    public int getPossibleTakingAmountOfMoney() {
        return this.possibleTakingAmountOfMoney;
    }

    public void setRanks(long ranks) {
        this.ranks = ranks;
    }

    public long getRanks() {
        return this.ranks;
    }

    public enum RoomStatus {
        WAITING,
        READY
    }
}
