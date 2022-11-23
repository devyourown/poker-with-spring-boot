package org.example.backend.controllers;

import org.example.backend.dto.GameDTO;
import org.example.domain.card.Card;
import org.example.domain.game.Game;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class GameController {
    private HashMap<Long, Game> gameHashMap = new HashMap<>();

    @GetMapping("/game")
    public GameDTO getGame(@RequestParam(value = "gameId") Long id) {
        Game game = gameHashMap.get(id);
        return new GameDTO(game);
    }

    @PostMapping("/action")
    public void postBet(@RequestParam(value = "gameId") Long id,
                        @RequestParam(value = "playerId") Long playerId,
                        @RequestParam(value = "action") String action,
                        @RequestParam(value = "betSize") int betSize) {

    }

    @GetMapping("/flop")
    public List<Card> getFlop(@RequestParam(value = "gameId") Long id) {
        Game game = gameHashMap.get(id);
        return game.getBoard();
    }

    @GetMapping("/turn")
    public List<Card> getTurn(@RequestParam(value = "gameId") Long id) {
        Game game = gameHashMap.get(id);
        return game.getBoard();
    }

    @GetMapping("/river")
    public List<Card> getRiver(@RequestParam(value = "gameId") Long id) {
        Game game = gameHashMap.get(id);
        return game.getBoard();
    }
}
