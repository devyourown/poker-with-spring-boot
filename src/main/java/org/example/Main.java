package org.example;

import org.example.console.ConsoleInput;
import org.example.console.ConsoleOutput;
import org.example.console.UserAction;
import org.example.domain.game.Action;
import org.example.domain.game.Dealer;
import org.example.domain.game.Game;
import org.example.domain.game.GameStatus;
import org.example.domain.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Player> players;
    public static void main(String[] args) {
        System.out.println("---------게임 시작----------");
        System.out.println();
        players = new ArrayList<>();
        players.add(new Player(10000));
        players.add(new Player(5000));
        players.add(new Player(6000));
        try {
            Game game = new Game(players, 100, 200);
            startGame(game);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void startGame(Game game) throws Exception {
        while (!game.isEnd()) {
            playUntilStatus(game, GameStatus.PRE_FLOP);
            if (game.isEnd())
                break ;
            ConsoleOutput.printBoard(game.getBoard());
            playUntilStatus(game, GameStatus.FLOP);
            if (game.isEnd())
                break ;
            ConsoleOutput.printBoard(game.getBoard());
            playUntilStatus(game, GameStatus.TURN);
            if (game.isEnd())
                break ;
            ConsoleOutput.printBoard(game.getBoard());
            playUntilStatus(game, GameStatus.RIVER);
        }
        ConsoleOutput.printGameResult(game);
    }

    private static void playUntilStatus(Game game, GameStatus status) throws Exception {
        int index = 0;
        while (game.getStatus() == status) {
            ConsoleOutput.printPotSize(game.getBettingSize(), game.getPot());
            ConsoleOutput.printHands(game
                    .getPlayerHandsOf(index % game.getSizeOfPlayers()));
            ConsoleOutput.printAvailableAction(game.getBettingSize(), index);
            UserAction userAction = ConsoleInput.getUserAction(game.getBettingSize(), index);
            game.playAction(index % game.getSizeOfPlayers(),
                    userAction.action, userAction.betSize);
            if (userAction.action == Action.FOLD)
                index--;
            index++;
        }
    }
}