package org.example.domain.game.helper;

import org.example.domain.card.Card;
import org.example.domain.deck.Deck;
import org.example.domain.game.GameStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dealer {
    private Deck deck;
    private List<Card> board;
    private int numOfPlayer;
    private GameStatus gameStatus;

    public Dealer(int numOfPlayer) {
        this.numOfPlayer = numOfPlayer;
        deck = new Deck(numOfPlayer);
        board = new ArrayList<>();
        gameStatus = GameStatus.PRE_FLOP;
    }

    public void setBoard() {
        gameStatus = gameStatus.nextStatus();
        if (gameStatus == GameStatus.FLOP)
            setFlop();
        else if (gameStatus == GameStatus.TURN)
            setTurn();
        else if (gameStatus == GameStatus.RIVER)
            setRiver();
    }

    public void reset() {
        deck = new Deck(this.numOfPlayer);
        board.clear();
    }

    private void setFlop() {
        board.addAll(getFlopCards());
    }

    private void setTurn() {
        board.add(getTurnCard());
    }

    private void setRiver() {
        board.add(getRiverCard());
    }

    public List<Card> handoutCards() {
        return List.of(deck.draw(), deck.draw());
    }

    private List<Card> getFlopCards() {
        return List.of(deck.draw(), deck.draw(), deck.draw());
    }

    private Card getTurnCard() {
        return deck.draw();
    }

    private Card getRiverCard() {
        return deck.draw();
    }

    public List<Card> getBoard() {
        return Collections.unmodifiableList(board);
    }
}
