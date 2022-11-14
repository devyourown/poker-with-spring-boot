package org.example.domain.deck;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {
    private Stack<Card> deck;

    public Deck() {
        deck = initDeck();
    }

    private Stack<Card> initDeck() {
        Stack<Card> result = new Stack<>();
        for (Suit suit : Suit.values()) {
            result.addAll(makeCardsWith(suit));
        }
        Collections.shuffle(result);
        return result;
    }

    private List<Card> makeCardsWith(Suit suit) {
        List<Card> result = new ArrayList<>();
        for (int i=1; i<=13; i++) {
            result.add(new Card(i, suit));
        }
        return result;
    }

    public int getNumOfCards() {
        return deck.size();
    }

    public Card draw() {
        return deck.pop();
    }

}
