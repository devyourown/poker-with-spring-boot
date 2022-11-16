package org.example;

import org.example.domain.game.Dealer;
import org.example.domain.game.Game;
import org.example.domain.game.GameStatus;

public class Main {
    public static void main(String[] args) {
        System.out.println("---------게임 시작----------");

    }

    public void startGame(Game game) {
        while (!game.isEnd()) {
            while (game.getStatus() == GameStatus.PRE_FLOP) {
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