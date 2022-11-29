package org.example;

import org.example.console.ConsoleInput;
import org.example.console.ConsoleOutput;
import org.example.console.UserAction;
import org.example.domain.game.Action;
import org.example.domain.game.Game;
import org.example.domain.game.GameStatus;
import org.example.domain.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<Player> players;
    public static void main(String[] args) {
        System.out.println("---------게임 시작----------");
        System.out.println();
        players = new ArrayList<>();
        players.add(new Player("1", 10000));
        players.add(new Player("2", 5000));
        players.add(new Player("3", 6000));

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