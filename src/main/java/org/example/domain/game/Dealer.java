package org.example.domain.game;

import org.example.domain.card.Card;
import org.example.domain.deck.Deck;
import org.example.domain.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dealer {
    private Deck deck;
    private List<Card> board;

    public Dealer() {
        deck = new Deck();
        board = new ArrayList<>();
    }

    public void setBoardAsStatus(GameStatus status) {
        if (status == GameStatus.FLOP)
            setFlop();
        else if (status == GameStatus.TURN)
            setTurn();
        else if (status == GameStatus.RIVER)
            setRiver();
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
