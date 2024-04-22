package org.example.domain.player;

import org.example.domain.card.Card;
import org.example.domain.game.helper.Pot;
import org.example.domain.rules.RankingCalculator;
import org.example.domain.rules.Ranking;

import java.util.Collections;
import java.util.List;

public class Player {
    private String id;
    private int money;
    private List<Card> hands;
    private int ranks;
    private RoomStatus roomStatus;
    private PlayingStatus playingStatus;

    public Player(String id, int money) {
        this.id = id;
        this.money = money;
        this.roomStatus = RoomStatus.WAITING;
        setPlayingStatus();
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


    public boolean isAllIn() {
        return playingStatus == PlayingStatus.ALLIN;
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

    public void setPlayingStatus() {
        if (money == 0)
            playingStatus = PlayingStatus.ALLIN;
        else
            playingStatus = PlayingStatus.PLAYING;
    }

    public void setCallStatus() {
        playingStatus = PlayingStatus.CALL;
    }

    public PlayingStatus getPlayingStatus() {
        return playingStatus;
    }

    public void setRanks(int ranks) {
        this.ranks = ranks;
    }

    public enum RoomStatus {
        WAITING,
        READY
    }

    public enum PlayingStatus {
        PLAYING,
        ALLIN,
        CALL
    }
}
