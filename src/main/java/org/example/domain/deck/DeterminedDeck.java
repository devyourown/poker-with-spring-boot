package org.example.domain.deck;

import org.example.domain.card.Card;

import java.util.List;

public class DeterminedDeck implements Deck {
    List<Card> deck;

    public DeterminedDeck(List<Card> deck) {
        this.deck = deck;
    }

    @Override
    public Card draw() {
        return deck.get(deck.size()-1);
    }

    @Override
    public void reset(int numOfPlayers) {
    }
}
