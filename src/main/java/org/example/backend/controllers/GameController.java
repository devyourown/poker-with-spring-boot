package org.example.backend.controllers;

import org.example.backend.dto.GameDTO;
import org.example.domain.card.Card;
import org.example.domain.game.Game;
import org.example.domain.player.Player;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
public class GameController {
    private HashMap<Long, Game> gameHashMap = new HashMap<>();

    @GetMapping("/game")
    public GameDTO getGame(@RequestParam(value = "gameId") Long id) {
        Game game = gameHashMap.get(id);
        return new GameDTO(game);
    }

    @PostMapping("/game")
    public Long makeGame(@RequestParam(value = "arr[]") List<Player> players) {
        Game game = new Game(players, 100, 200);
        Long id = Long.parseLong(UUID.randomUUID().toString());
        gameHashMap.put(id, game);
        return id;
    }

    @GetMapping("/game/hands")
    public List<Card> getHands(@RequestParam(value = "gameId") Long id,
                               @RequestParam(value = "playerId") Long playerId) {
        Game game = gameHashMap.get(id);
        for (Player player : game.getPlayers()) {
            //playerID가 같으면 핸즈를 반환한다.
        }
        return Collections.emptyList();
    }

    @PostMapping("/action")
    public void postBet(@RequestParam(value = "gameId") Long id,
                        @RequestParam(value = "playerId") Long playerId,
                        @RequestParam(value = "action") String action,
                        @RequestParam(value = "betSize") int betSize) {

    }
}
