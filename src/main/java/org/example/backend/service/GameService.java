package org.example.backend.service;

import org.example.backend.dto.*;
import org.example.domain.card.Card;
import org.example.domain.error.BetException;
import org.example.domain.error.RoomException;
import org.example.domain.game.Action;
import org.example.domain.game.Game;
import org.example.domain.game.GameStatus;
import org.example.domain.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private RoomService roomService;

    private HashMap<String, Game> gameHashMap = new HashMap<>();
    public boolean hasPlayerInGame(String gameId, String playerId) {
        validateGame(gameId);
        return gameHashMap.get(gameId).hasThisPlayer(playerId);
    }

    private void validateGame(String gameId) {
        if (!gameHashMap.containsKey(gameId))
            throw new IllegalArgumentException("There's no game with this id.");
    }

    public GameDTO makeGame(RoomDTO roomDTO) throws RoomException {
        validateRoom(roomDTO);
        Room room = roomService.getRoom(roomDTO.getRoomId());
        Game game = new Game(room.getPlayers(), roomDTO.getSmallBlind(), roomDTO.getBigBlind());
        String gameId = UUID.randomUUID().toString();
        gameHashMap.put(gameId, game);
        return getCurrentGame(gameId);
    }

    private void validateRoom(RoomDTO roomDTO) throws RoomException {
        if (roomDTO.getPlayers().size() == 1)
            throw new RoomException(RoomException.ErrorCode.NOT_ENOUGH_PLAYER);
    }

    public List<CardDTO> getHands(String gameId, String playerId) {
        List<Card> hands = gameHashMap.get(gameId).getHandsOf(playerId);
        List<CardDTO> result = new ArrayList<>();
        for (int i=0; i<2; i++) {
            result.add(CardDTO.builder()
                    .suit(hands.get(i).getSuit())
                    .value(hands.get(i).getValue())
                    .build());
        }
        return result;
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

    public GameDTO getCurrentGame(String gameId) {
        Game game = gameHashMap.get(gameId);
        GameDTO gameDTO = GameDTO.builder()
                .board(getCardDTOs(game.getBoard()))
                .currentBet(game.getBettingSize())
                .potSize(game.getPot())
                .gameStatus(game.getStatus())
                .build();
        if (game.getStatus() == GameStatus.END)
            removeGame(gameId);
        return gameDTO;
    }

    private List<CardDTO> getCardDTOs(final List<Card> board) {
        List<CardDTO> result = new ArrayList<>();
        for (Card card : board) {
            result.add(CardDTO
                    .builder()
                    .suit(card.getSuit())
                    .value(card.getValue())
                    .build());
        }
        return result;
    }

    private void removeGame(String gameId) {
        gameHashMap.remove(gameId);
    }
}
