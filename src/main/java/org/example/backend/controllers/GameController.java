package org.example.backend.controllers;

import org.example.backend.dto.ActionDTO;
import org.example.backend.dto.GameDTO;
import org.example.backend.dto.RoomDTO;
import org.example.backend.service.GameService;
import org.example.domain.card.Card;
import org.example.domain.game.Action;
import org.example.domain.game.Game;
import org.example.domain.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
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
    public GameDTO getGame(@AuthenticationPrincipal String playerId,
                           @RequestParam(value = "gameId") Long id) {
        Game game = gameHashMap.get(id);
        return new GameDTO(game);
    }

    @PostMapping("/new-game")
    public String makeGame(@RequestBody RoomDTO roomDTO) {
        Game game = new Game(roomDTO.getPlayers(), 100, 200);
        String gameId = UUID.randomUUID().toString();
        gameHashMap.put(gameId, game);
        return gameId;
    }

    @PostMapping("/re-game")
    public void resetGame(@RequestBody RoomDTO roomDTO) {

    }

    @GetMapping("/hands")
    public List<Card> getHands(@AuthenticationPrincipal String playerId,
                               @RequestParam(value = "gameId") String gameId) {
        Game game = gameHashMap.get(gameId);
        for (Player player : game.getPlayers()) {
            if (player.getId() == playerId)
                return player.getHands();
        }
        return Collections.emptyList();
    }

    @PostMapping("/action")
    public void postBet(@AuthenticationPrincipal String playerId,
                        @RequestBody ActionDTO actionDTO) {
        Game game = gameHashMap.get(actionDTO.getGameId());
        Action action = actionDTO.getAction();
        for (Player player : game.getPlayers()) {
            if (player.getId() == playerId) {
                int playerIndex = game.getPlayers().indexOf(player);
                game.playAction(playerIndex, action, actionDTO.getBetSize());
            }
        }
    }
}
