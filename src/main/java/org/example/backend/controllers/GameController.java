package org.example.backend.controllers;

import org.example.backend.dto.ActionDTO;
import org.example.backend.dto.GameDTO;
import org.example.backend.dto.HandsDTO;
import org.example.backend.dto.RoomDTO;
import org.example.backend.service.GameService;
import org.example.domain.card.Card;
import org.example.domain.error.BetException;
import org.example.domain.error.RoomException;
import org.example.domain.game.Action;
import org.example.domain.game.Game;
import org.example.domain.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/result")
    public ResponseEntity<?> getGame(@AuthenticationPrincipal String playerId,
                                  @RequestParam(value = "gameId") String gameId) {
        if (!gameService.hasPlayerInGame(gameId, playerId))
            return ResponseEntity.badRequest().body("error: not player");
        return ResponseEntity.ok(gameService.getCurrentGame(gameId));
    }

    @PostMapping("/game")
    public ResponseEntity<?> makeGame(@RequestBody RoomDTO roomDTO) {
        GameDTO gameDTO;
        try {
            gameDTO = gameService.makeGame(roomDTO);
        } catch (RoomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(gameDTO);
    }

    @GetMapping("/hands")
    public ResponseEntity<?> getHands(@AuthenticationPrincipal String playerId,
                               @RequestParam(value = "gameId") String gameId) {
        if (!gameService.hasPlayerInGame(gameId, playerId))
            return ResponseEntity.badRequest().body("error: not player");
        HandsDTO handsDTO = HandsDTO.builder()
                .hands(gameService.getHands(gameId, playerId))
                .build();
        return ResponseEntity.ok(handsDTO);
    }

    @PostMapping("/action")
    public ResponseEntity<?> playAction(@AuthenticationPrincipal String playerId,
                        @RequestBody ActionDTO actionDTO) {
        try {
            gameService.playAction(playerId, actionDTO);
        } catch (BetException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("success");
    }
}
