package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.game.GameStatus;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private String gameId;
    private int turnIndex;
    private boolean isMyTurn;
    private List<CardDTO> board;
    private int currentBet;
    private int potSize;
    private GameStatus gameStatus;
 }
