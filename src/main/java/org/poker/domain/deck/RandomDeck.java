package org.poker.domain.deck;

import org.poker.domain.card.Card;
import org.poker.domain.card.Suit;

import java.util.*;

public class RandomDeck implements Deck {
    private List<Card> deck;

    public RandomDeck(int numOfPlayer) {
        initDeck(numOfPlayer);
    }

    private void initDeck(int numOfPlayer) {
        int neededNumOfCards = numOfPlayer * 2 + 5;
        Set<Card> cards = new HashSet<>();
        while (cards.size() != neededNumOfCards) {
            cards.add(makeRandomCard());
        }
        deck = new ArrayList<>(cards);
    }

    private Card makeRandomCard() {
        int value = (int) (Math.random() * 13 + 1);
        Suit suit = Suit.values()[(int) (Math.random() * 4)];
        return Card.of(value, suit);
    }

    @Override
    public Card draw() {
        return deck.remove(deck.size() - 1);
    }

    public void reset(int numOfPlayers) {
        initDeck(numOfPlayers);
    }

}
