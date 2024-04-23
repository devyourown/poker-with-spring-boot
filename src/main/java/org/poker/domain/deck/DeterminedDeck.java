package org.poker.domain.deck;

import org.poker.domain.card.Card;

import java.util.List;

public class DeterminedDeck implements Deck {
    List<Card> deck;

    public DeterminedDeck(List<Card> deck) {
        this.deck = deck;
    }

    @Override
    public Card draw() {
        return deck.remove(deck.size()-1);
    }

    @Override
    public void reset(int numOfPlayers) {
    }
}
