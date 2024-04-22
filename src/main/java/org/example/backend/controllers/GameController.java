package org.example.backend.controllers;

import org.example.backend.dto.*;
import org.example.backend.security.UserAdapter;
import org.example.backend.service.GameService;
import org.example.backend.service.RoomService;
import org.example.domain.card.Card;
import org.example.domain.error.BetException;
import org.example.domain.game.Game;
import org.example.domain.player.Player;
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

    @PostMapping("/restart")
    public ResponseEntity<?> restartGame(@AuthenticationPrincipal UserAdapter user) throws Exception {
        Game game = gameService.getGamePlayerIn(user.getUserId());
        if (!game.isEnd())
            return ResponseEntity.badRequest().body("Not end");
        game.resetGame();
        return ResponseEntity.ok("Ok");
    }


    @PostMapping("/action")
    public ResponseEntity<?> playAction(@AuthenticationPrincipal UserAdapter user,
                        @RequestBody ActionDTO actionDTO) throws BetException{
        gameService.playAction(actionDTO);
        return ResponseEntity.ok("success");
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
