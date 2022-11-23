package org.example.backend.dto;

import org.example.domain.card.Card;

import java.util.Collections;
import java.util.List;

public class HandsDTO {
    private List<Card> hands;

    public List<Card> getHands() {
        return Collections.unmodifiableList(hands);
    }

    public void setHands(List<Card> hands) {
        this.hands = hands;
    }
}
