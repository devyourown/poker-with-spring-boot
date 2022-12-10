package org.example.backend.service;

import org.example.backend.dto.*;
import org.example.domain.card.Card;
import org.example.domain.error.BetException;
import org.example.domain.error.RoomException;
import org.example.domain.game.Action;
import org.example.domain.game.Game;
import org.example.domain.game.GameStatus;
import org.example.domain.player.Player;
import org.example.domain.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Game game = gameHashMap.get(gameId);
        if (game.getStatus() == GameStatus.END)
            removeGame(gameId);
        return game;
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

    public void leaveGame(String playerId) throws Exception {
        String gameId = playerGameId.get(playerId);
        validateGame(gameId);
        gameHashMap.get(gameId).removePlayer(playerId);
    }

    public List<Card> getHands(String playerId) throws Exception {
        String gameId = playerGameId.get(playerId);
        validateGame(gameId);
        return gameHashMap.get(gameId).getHandsOf(playerId);
    }

    private void validateGame(String gameId) throws Exception {
        if (!gameHashMap.containsKey(gameId))
            throw new IllegalArgumentException("There's no game with this id.");
    }

    public void playAction(String playerId, ActionDTO actionDTO) throws BetException {
        validateAction(playerId, actionDTO);
        Game game = gameHashMap.get(actionDTO.getGameId());
        Action action = actionDTO.getAction();
        game.playAction(action, actionDTO.getBetSize());
    }

    private void validateAction(String playerId, ActionDTO actionDTO) throws BetException {
        Game game = gameHashMap.get(actionDTO.getGameId());
        if (!game.isCurrentTurn(playerId))
            throw new BetException(BetException.ErrorCode.NOT_YOUR_TURN);
        if (actionDTO.getAction() == Action.BET) {
            if (game.getBettingSize() > actionDTO.getBetSize())
                throw new BetException(BetException.ErrorCode.MONEY_NOT_ENOUGH);
            if (hasLessThanHundred(game.getBettingSize()))
                throw new BetException(BetException.ErrorCode.INVALID_BET_SIZE);
        }
        if (game.getBettingSize() > 0) {
            if (actionDTO.getAction() == Action.CHECK)
                throw new BetException(BetException.ErrorCode.NOT_POSSIBLE_CHECK);
        }
    }

    private boolean hasLessThanHundred(int money) {
        return money % 100 != 0;
    }

    private void removeGame(String gameId) {
        gameHashMap.remove(gameId);
    }
}
