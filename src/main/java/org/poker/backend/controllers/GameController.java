package org.poker.backend.controllers;

import org.poker.backend.security.UserAdapter;
import org.poker.backend.service.GameService;
import org.poker.backend.service.RoomService;
import org.poker.domain.card.Card;
import org.poker.domain.error.BetException;
import org.poker.domain.game.Game;
import org.poker.backend.dto.ActionDTO;
import org.poker.backend.dto.CardDTO;
import org.poker.backend.dto.HandsDTO;
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
