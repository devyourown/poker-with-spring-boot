package org.example.backend.controllers;

import org.example.backend.dto.ActionDTO;
import org.example.backend.dto.GameDTO;
import org.example.backend.dto.HandsDTO;
import org.example.backend.dto.RoomDTO;
import org.example.backend.service.GameService;
import org.example.domain.card.Card;
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

    private HashMap<String, Game> gameHashMap = new HashMap<>();

    @GetMapping("/result")
    public ResponseEntity<?> getGame(@AuthenticationPrincipal String playerId,
                                  @RequestParam(value = "gameId") String gameId) {
        if (!gameService.hasPlayerInGame(gameId, playerId))
            ResponseEntity.badRequest().body("error: not player");
        Game game = gameHashMap.get(gameId);
        return ResponseEntity.ok().body(new GameDTO());
    }

    @PostMapping("/new-game")
    public String makeGame(@RequestBody RoomDTO roomDTO) {
        return gameService.makeGame(roomDTO);
    }

    @PostMapping("/re-game")
    public void resetGame(@RequestBody RoomDTO roomDTO) {
        //should change game domain
    }

    @GetMapping("/hands")
    public ResponseEntity<?> getHands(@AuthenticationPrincipal String playerId,
                               @RequestParam(value = "gameId") String gameId) {
        if (!gameService.hasPlayerInGame(gameId, playerId))
            ResponseEntity.badRequest().body("error: not player");
        HandsDTO handsDTO = HandsDTO.builder()
                .hands(gameService.getHands(gameId, playerId))
                .build();
        return ResponseEntity.ok(handsDTO);
    }

    @PostMapping("/action")
    public void postBet(@AuthenticationPrincipal String playerId,
                        @RequestBody ActionDTO actionDTO) {
        gameService.playAction(playerId, actionDTO);
    }
}
