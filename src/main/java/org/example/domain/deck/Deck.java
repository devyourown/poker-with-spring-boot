package org.example.domain.deck;

import org.example.domain.card.Card;

public interface Deck {
    Card draw();
    void reset(int numOfPlayers);
}
