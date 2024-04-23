package org.poker.backend.service;

import org.poker.domain.error.BetException;
import org.poker.domain.game.Action;
import org.poker.domain.game.Game;
import org.poker.domain.player.Player;
import org.poker.domain.room.Room;
import org.poker.backend.dto.ActionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class GameService {

    @Autowired
    private RoomService roomService;

    private HashMap<String, Game> gameHashMap = new HashMap<>();
    private HashMap<String, String> playerGameId = new HashMap<>();
    private HashMap<String, Game> roomIdWithGame = new HashMap();

    public Game getGamePlayerIn(String playerId) throws Exception {
        String gameId = playerGameId.get(playerId);
        validateGame(gameId);
        return gameHashMap.get(gameId);
    }

    public Game makeGame(Room room) throws Exception {
        if (roomIdWithGame.containsKey(room.getId()))
            return roomIdWithGame.get(room.getId());
        Game game = roomService.makeGame(room.getId());
        registerPlayerInGame(game.getGameId(), room.getPlayers());
        gameHashMap.put(game.getGameId(), game);
        roomIdWithGame.put(room.getId(), game);
        return game;
    }

    private void registerPlayerInGame(String gameId, List<Player> players) {
        for (Player player : players) {
            playerGameId.put(player.getId(), gameId);
        }
    }

    private void validateGame(String gameId) throws Exception {
        if (!gameHashMap.containsKey(gameId))
            throw new IllegalArgumentException("There's no game with this id.");
    }

    public void playAction(ActionDTO actionDTO) throws BetException {
        Game game = gameHashMap.get(actionDTO.getGameId());
        Action action = actionDTO.getAction();
    }

    private boolean hasLessThanHundred(int money) {
        return money % 100 != 0;
    }

    private void removeGame(String gameId) {
        Game game = gameHashMap.get(gameId);
        if (game.isEnd())
            gameHashMap.remove(gameId);
    }
}
