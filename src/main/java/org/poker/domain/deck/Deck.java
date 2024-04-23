package org.poker.domain.deck;

import org.poker.domain.card.Card;

public interface Deck {
    Card draw();
    void reset(int numOfPlayers);
}
