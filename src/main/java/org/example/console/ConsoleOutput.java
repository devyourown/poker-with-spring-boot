package org.example.console;

import org.example.domain.card.Card;
import org.example.domain.game.Action;
import org.example.domain.game.Game;
import org.example.domain.game.helper.Dealer;
import org.example.domain.game.helper.Pot;
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

    public static void printPotSize(Pot pot) {
        System.out.println("PotSize : " + pot.getTotalAmount());
        System.out.println("BettingSize : " + pot.getCurrentBet());
    }

    public static void printHands(List<Card> hands) {
        System.out.println("Hands : ");
        for (Card card : hands) {
            System.out.print(card.getValue() + " : " + card.getSuit());
            System.out.print("  ");
        }
        System.out.println();
    }

    public static void printAvailableAction(Pot pot) {
        System.out.print("선택할 수 있는 액션 : ");
        if (pot.getCurrentBet() > 0)
            System.out.println(Action.FOLD.toString() + " " + Action.BET + " " + Action.CALL);
        else
            System.out.println(Action.FOLD.toString() + " " + Action.BET + " " + Action.CHECK);
    }

    public static void printMoney(int money) {
        System.out.println("현재 갖고 있는 돈 : " + money);
    }

    public static void printForAction(Game game, Pot pot, Dealer dealer) {
        if (dealer.isAfterPreFlop())
            ConsoleOutput.printBoard(dealer.getBoard());
        ConsoleOutput.printPotSize(pot);
        ConsoleOutput.printMoney(game.getCurrentPlayerMoney());
        ConsoleOutput.printHands(game.getCurrentPlayerHands());
        ConsoleOutput.printAvailableAction(pot);
    }

}
