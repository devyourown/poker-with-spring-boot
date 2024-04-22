package org.example.console;

import org.example.domain.game.Action;
import org.example.domain.game.Game;
import org.example.domain.game.GameStatus;
import org.example.domain.game.helper.GameResult;
import org.example.domain.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ConsoleGame {
    private static List<Player> players;

    public static void start() {
        System.out.println("---------Game start----------");
        System.out.println();
        players = ConsoleInput.initUserFromInput();
        Game game = new Game(players, 100, 200);
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
