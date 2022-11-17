package org.example;

import org.example.console.ConsoleInput;
import org.example.console.UserAction;
import org.example.domain.game.Action;
import org.example.domain.game.Dealer;
import org.example.domain.game.Game;
import org.example.domain.game.GameStatus;
import org.example.domain.player.Player;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Player> players;
    public static void main(String[] args) {
        System.out.println("---------게임 시작----------");
        System.out.println();
    }

    public void startGame(Game game) throws Exception {
        int index = 0;
        while (!game.isEnd()) {
            while (game.getStatus() == GameStatus.PRE_FLOP) {
                UserAction userAction = ConsoleInput.getUserAction();
                game.playAction(index % players.size(),
                        userAction.action, userAction.betSize);
            }
            game.setFlop();
            while (game.getStatus() == GameStatus.FLOP) {
            }
            game.setTurn();
            while (game.getStatus() == GameStatus.TURN) {
            }
            game.setRiver();
            while (game.getStatus() == GameStatus.RIVER) {

            }
        }
    }
}