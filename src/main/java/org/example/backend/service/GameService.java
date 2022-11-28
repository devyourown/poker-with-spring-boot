package org.example.backend.service;

import org.example.domain.game.Game;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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
}
