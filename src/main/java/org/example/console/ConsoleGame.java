package org.example.console;

import org.example.domain.game.Action;
import org.example.domain.game.Game;
import org.example.domain.game.GameStatus;
import org.example.domain.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ConsoleGame {
    private static List<Player> players;

    public static void start() {
        System.out.println("---------게임 시작----------");
        System.out.println();
        players = ConsoleInput.initUserFromInput();
        Game game = new Game(players, 100, 200);
        startGame(game);
    }

    private static void startGame(Game game) {
        while (!game.isEnd()) {
            playUntilStatus(game, GameStatus.PRE_FLOP);
            if (game.isEnd())
                break ;
            playUntilStatus(game, GameStatus.FLOP);
            if (game.isEnd())
                break ;
            playUntilStatus(game, GameStatus.TURN);
            if (game.isEnd())
                break ;
            playUntilStatus(game, GameStatus.RIVER);
        }
    }

    private static void playUntilStatus(Game game, GameStatus status) {
        int index = 0;
        while (game.getStatus() == status) {
            printForAction(game, index);
            UserAction userAction = ConsoleInput.getUserAction(game.getBettingSize(), index);
            game.playAction(userAction.action, userAction.betSize);
            if (userAction.action == Action.FOLD)
                index--;
            index++;
        }
    }

    private static void printForAction(Game game, int index) {
        if (!game.getBoard().isEmpty())
            ConsoleOutput.printBoard(game.getBoard());
        ConsoleOutput.printPotSize(game.getBettingSize(), game.getPot());
        ConsoleOutput.printMoney(game.getPlayerMoneyOf(index % game.getSizeOfPlayers()));
        ConsoleOutput.printHands(game.getPlayerHandsOf(index % game.getSizeOfPlayers()));
        ConsoleOutput.printAvailableAction(game.getBettingSize(), index);
    }
}
