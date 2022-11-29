package org.example.backend.service;

import org.example.backend.dto.ActionDTO;
import org.example.backend.dto.GameDTO;
import org.example.backend.dto.RoomDTO;
import org.example.domain.card.Card;
import org.example.domain.game.Action;
import org.example.domain.game.Game;
import org.example.domain.game.GameStatus;
import org.example.domain.player.Player;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    private HashMap<String, Game> gameHashMap = new HashMap<>();
    public boolean hasPlayerInGame(String gameId, String playerId) {
        validateGame(gameId);
        return gameHashMap.get(gameId).hasThisPlayer(playerId);
    }

    private void validateGame(String gameId) {
        if (!gameHashMap.containsKey(gameId))
            throw new IllegalArgumentException("There's no game with this id.");
    }

    public String makeGame(RoomDTO roomDTO) {
        Game game = new Game(roomDTO.getPlayers(), roomDTO.getSmallBlind(), roomDTO.getBigBlind());
        String gameId = UUID.randomUUID().toString();
        gameHashMap.put(gameId, game);
        return gameId;
    }

    public List<Card> getHands(String gameId, String playerId) {
        return gameHashMap.get(gameId).getHandsOf(playerId);
    }

    public void playAction(String playerId, ActionDTO actionDTO) {
        Game game = gameHashMap.get(actionDTO.getGameId());
        Action action = actionDTO.getAction();
        if (game.isCurrentTurn(playerId))
            game.playAction(action, actionDTO.getBetSize());
    }

    public GameDTO getCurrentGame(String gameId) {
        Game game = gameHashMap.get(gameId);
        GameDTO gameDTO = GameDTO.builder()
                .board(game.getBoard())
                .currentBet(game.getBettingSize())
                .potSize(game.getPot())
                .gameStatus(game.getStatus())
                .build();
        if (game.getStatus() == GameStatus.END)
            removeGame(gameId);
        return gameDTO;
    }

    private void removeGame(String gameId) {
        gameHashMap.remove(gameId);
    }
}
