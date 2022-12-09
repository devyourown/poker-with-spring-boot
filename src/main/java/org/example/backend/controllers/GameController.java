package org.example.backend.controllers;

import org.example.backend.dto.*;
import org.example.backend.security.UserAdapter;
import org.example.backend.service.GameService;
import org.example.backend.service.RoomService;
import org.example.domain.card.Card;
import org.example.domain.error.BetException;
import org.example.domain.game.Game;
import org.example.domain.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private RoomService roomService;

    @GetMapping("/result")
    public ResponseEntity<?> getGame(@AuthenticationPrincipal UserAdapter user) throws Exception {
        Game game = gameService.getGamePlayerIn(user.getUserId());
        return ResponseEntity.ok(getGameDTO(game));
    }

    @PostMapping("/game")
    public ResponseEntity<?> makeGame(@AuthenticationPrincipal UserAdapter user) throws Exception {
        Room room = roomService.getRoomPlayerIn(user.getUserId());
        Game game = gameService.makeGame(room);
        return ResponseEntity.ok(getGameDTO(game));
    }

    @GetMapping("/hands")
    public ResponseEntity<?> getHands(@AuthenticationPrincipal UserAdapter user) throws Exception {
        List<Card> hands = gameService.getHands(user.getUserId());
        return ResponseEntity.ok(getHandsDTO(getCardDTOs(hands)));
    }

    @PostMapping("/leave")
    public ResponseEntity<?> leaveGame(@AuthenticationPrincipal UserAdapter user) throws Exception {
        gameService.leaveGame(user.getUserId());
        return ResponseEntity.ok("success");
    }

    @PostMapping("/action")
    public ResponseEntity<?> playAction(@AuthenticationPrincipal UserAdapter user,
                        @RequestBody ActionDTO actionDTO) throws BetException{
        gameService.playAction(user.getUserId(), actionDTO);
        return ResponseEntity.ok("success");
    }

    private GameDTO getGameDTO(Game game) {
        return GameDTO.builder()
                .gameId(game.getGameId())
                .board(getCardDTOs(game.getBoard()))
                .currentBet(game.getBettingSize())
                .turnIndex(game.currentTurnIndex())
                .potSize(game.getPot())
                .gameStatus(game.getStatus())
                .lastAction(game.getLastAction())
                .lastActionIndex(game.getLastActionIndex())
                .build();
    }

    private List<CardDTO> getCardDTOs(List<Card> cards) {
        List<CardDTO> result = new ArrayList<>();
        for (Card card : cards) {
            result.add(CardDTO.builder()
                    .suit(card.getSuit())
                    .value(card.getValue())
                    .build());
        }
        return result;
    }

    private HandsDTO getHandsDTO(List<CardDTO> hands) {
        return HandsDTO.builder()
                .hands(hands)
                .build();
    }
}
