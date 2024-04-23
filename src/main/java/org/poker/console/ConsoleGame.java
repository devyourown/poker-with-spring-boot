package org.poker.console;

import org.poker.domain.deck.RandomDeck;
import org.poker.domain.game.Game;
import org.poker.domain.game.helper.GameResult;
import org.poker.domain.player.Player;

import java.util.List;

public class ConsoleGame {
    private static List<Player> players;

    public static void start() {
        System.out.println("---------Game start----------");
        System.out.println();
        players = ConsoleInput.initUserFromInput();
        Game game = new Game(players, 100, 200, new RandomDeck(players.size()));
        startGame(game);
    }

    private static void startGame(Game game) {
        boolean isEnd = false;
        while (!isEnd) {
            GameResult result = game.play();
            System.out.println(result);
            isEnd = ConsoleInput.askPlayAgain();
            game.resetGame();
        }
    }
}
