package org.example.console;

import org.example.domain.card.Card;
import org.example.domain.game.Action;
import org.example.domain.game.Game;
import org.example.domain.player.Player;

import java.util.List;

public class ConsoleOutput {
    public static void printBoard(List<Card> board) {
        System.out.println("Board : ");
        for (Card card : board) {
            System.out.print(card.getValue() + " : " + card.getSuit());
            System.out.print("  ");
        }
        System.out.println();
    }

    public static void printPotSize(int currentBet, int potSize) {
        System.out.println("PotSize : " + potSize);
        System.out.println("BettingSize : " + currentBet);
    }

    public static void printHands(List<Card> hands) {
        System.out.println("Hands : ");
        for (Card card : hands) {
            System.out.print(card.getValue() + " : " + card.getSuit());
            System.out.print("  ");
        }
        System.out.println();
    }

    public static void printAvailableAction(int betSize, int gameOrder) {
        System.out.print("선택할 수 있는 액션 : ");
        if (betSize > 0)
            System.out.println(Action.FOLD.toString() + " " + Action.CALL + " " + Action.BET);
        else if (gameOrder == 0)
            System.out.println(Action.FOLD.toString() + " " + Action.BET + " " + Action.CHECK);
        else
            System.out.println(Action.FOLD.toString() + " " + Action.BET + " " + Action.CHECK);
    }

    public static void printMoney(int money) {
        System.out.println("현재 갖고 있는 돈 : " + money);
    }

    public static void printGameResult(Game game) {
        System.out.println("결과 : ");
        for (Player player : game.getPlayers()) {
            System.out.print("패 : " + player.getHands() + " ");
            System.out.println("돈 : " + player.getMoney() + " ");
        }
    }
}
