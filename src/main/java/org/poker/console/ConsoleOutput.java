package org.poker.console;

import org.poker.domain.card.Card;
import org.poker.domain.game.Action;
import org.poker.domain.game.helper.Dealer;
import org.poker.domain.game.helper.Pot;
import org.poker.domain.player.Player;

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

    private static void printPotSize(Pot pot, Player player) {
        System.out.println("PotSize : " + pot.getTotalAmount());
        System.out.println("BettingSize : " + pot.getCurrentBet());
        System.out.println("Size To CALL : " + pot.getSizeToCall(player));
    }

    private static void printPlayer(Player player) {
        System.out.println("Player ID : " + player.getId());
        printHands(player.getHands());
        printMoney(player.getMoney());
    }

    private static void printHands(List<Card> hands) {
        System.out.println("Hands : ");
        for (Card card : hands) {
            System.out.print(card.getValue() + " : " + card.getSuit());
            System.out.print("  ");
        }
        System.out.println();
    }

    public static void printMoney(int money) {
        System.out.println("Money you have : " + money);
    }

    public static void printAvailableAction(Pot pot) {
        System.out.print("Choose the Action : ");
        if (pot.getCurrentBet() > 0)
            System.out.println(Action.FOLD.toString() + " " + Action.BET + " " + Action.CALL);
        else
            System.out.println(Action.FOLD.toString() + " " + Action.BET + " " + Action.CHECK);
    }


    public static void printForAction(Pot pot, Dealer dealer, Player player) {
        if (dealer.isAfterPreFlop())
            ConsoleOutput.printBoard(dealer.getBoard());
        ConsoleOutput.printPlayer(player);
        ConsoleOutput.printPotSize(pot, player);
        ConsoleOutput.printAvailableAction(pot);
    }

}
