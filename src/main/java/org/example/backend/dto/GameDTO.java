package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.card.Card;
import org.example.domain.game.Game;
import org.example.domain.game.GameStatus;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private List<Card> board;
    private int currentBet;
    private int potSize;
    private GameStatus gameStatus;
 }
