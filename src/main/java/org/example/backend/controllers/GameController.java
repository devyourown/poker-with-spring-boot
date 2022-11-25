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
        //ID를 받고 DB에서 매치한 결과를 가져와야 될듯함.
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
            if (player.getId() == playerId)
                return player.getHands();
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
