package org.example.domain.game;

import org.example.domain.card.Card;
import org.example.domain.deck.Deck;
import org.example.domain.player.Player;

import java.util.Collections;
import java.util.List;

public class Dealer {
    private Deck deck;
    public Dealer() {
        deck = new Deck();
    }

    public List<Card> handoutCards() {
        return List.of(deck.draw(), deck.draw());
    }

    public List<Card> getFlopCards() {
        return List.of(deck.draw(), deck.draw(), deck.draw());
    }

    public List<Card> getTurnCard() {
        return List.of(deck.draw());
    }

    public List<Card> getRiverCard() {
        return List.of(deck.draw());
    }
}
