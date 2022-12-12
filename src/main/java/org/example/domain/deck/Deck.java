package org.example.domain.deck;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;

import java.util.*;
import java.util.stream.Collectors;

public class Deck {
    private List<Card> deck;

    public Deck(int numOfPlayer) {
        deck = initDeck(numOfPlayer);
    }

    private List<Card> initDeck(int numOfPlayer) {
        int neededNumOfCards = numOfPlayer * 2 + 5;
        Stack<Card> result = new Stack<>();
        Set<Card> cards = new HashSet<>();
        while (cards.size() != neededNumOfCards) {
            cards.add(makeRandomCard());
        }
        return cards.stream().collect(Collectors.toList());
    }

    private Card makeRandomCard() {
        int value = (int) (Math.random() * 13 + 1);
        Suit suit = Suit.values()[(int) (Math.random() * 4)];
        return Card.of(value, suit);
    }

    public int getNumOfCards() {
        return deck.size();
    }

    public Card draw() {
        return deck.remove(deck.size() - 1);
    }

}
