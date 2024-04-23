package org.example.domain.game.helper;

import org.example.domain.card.Card;
import org.example.domain.deck.Deck;
import org.example.domain.deck.Deck;
import org.example.domain.game.GameStatus;
import org.example.domain.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dealer {
    private Deck deck;
    private List<Card> board;
    private GameStatus gameStatus;
    private List<Player> players;

    public Dealer(List<Player> players, Deck deck) {
        this.players = players;
        this.deck = deck;
        board = new ArrayList<>();
        distributeCards();
        gameStatus = GameStatus.PRE_FLOP;
    }

    private void distributeCards() {
        for (Player player : players) {
            player.setHands(handoutCards());
        }
    }

    public void nextStatus() {
        gameStatus = gameStatus.nextStatus();
        if (gameStatus == GameStatus.FLOP)
            setFlop();
        else if (gameStatus == GameStatus.TURN)
            setTurn();
        else if (gameStatus == GameStatus.RIVER)
            setRiver();
    }

    public void reset(List<Player> players) {
        this.players = players;
        deck.reset(players.size());
        board.clear();
        gameStatus = GameStatus.PRE_FLOP;
        distributeCards();
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

    private List<Card> handoutCards() {
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

    public boolean isAfterPreFlop() {
        return gameStatus.compareTo(GameStatus.PRE_FLOP) > 0;
    }
}
