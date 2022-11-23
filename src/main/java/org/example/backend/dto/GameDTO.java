package org.example.backend.dto;

import org.example.domain.card.Card;
import org.example.domain.game.Game;

import java.util.List;

public class GameDTO {
    private List<Card> board;
    private int currentBet;
    private int potSize;

    public GameDTO(Game game) {
        board = game.getBoard();
        currentBet = game.getBettingSize();
        potSize = game.getPot();
    }
 }
