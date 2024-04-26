package org.poker.console;

import org.poker.domain.deck.RandomDeck;
import org.poker.domain.game.Game;
import org.poker.domain.game.helper.GameResult;
import org.poker.domain.player.Player;

import java.util.List;
import java.util.Scanner;

public class ConsoleGame {
    private static List<Player> players;
    private ConsoleInput input;
    private ConsoleOutput output;

    public void start(Scanner scanner) {
        System.out.println("---------Game start----------");
        System.out.println();
        input = new ConsoleInput(scanner);
        players = input.initUserFromInput();
        Game game = new Game(players, 100, 200, new RandomDeck(players.size()),
                input, new ConsoleOutput());
        startGame(game);
    }

    private void startGame(Game game) {
        boolean isEnd = false;
        while (!isEnd) {
            GameResult result = game.play();
            System.out.println(result);
            isEnd = input.askPlayAgain();
            game.resetGame();
        }
    }
}
