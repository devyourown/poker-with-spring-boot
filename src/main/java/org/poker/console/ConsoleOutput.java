package org.poker.console;

import org.poker.domain.card.Card;
import org.poker.domain.game.Action;
import org.poker.domain.game.Output;
import org.poker.domain.game.helper.Dealer;
import org.poker.domain.game.helper.Pot;
import org.poker.domain.player.Player;

import java.util.List;

public class ConsoleOutput implements Output {
    public void printBoard(List<Card> board) {
        System.out.println("Board : ");
        for (Card card : board) {
            System.out.print(card.getValue() + " : " + card.getSuit());
            System.out.print("  ");
        }
        System.out.println();
    }

    private void printPotSize(Pot pot, Player player) {
        System.out.println("PotSize : " + pot.getTotalAmount());
        System.out.println("BettingSize : " + pot.getCurrentBet());
        System.out.println("Size To CALL : " + pot.getSizeToCall(player));
    }

    private void printPlayer(Player player) {
        System.out.println("Player ID : " + player.getId());
        printHands(player.getHands());
        printMoney(player.getMoney());
    }

    private void printHands(List<Card> hands) {
        System.out.println("Hands : ");
        for (Card card : hands) {
            System.out.print(card.getValue() + " : " + card.getSuit());
            System.out.print("  ");
        }
        System.out.println();
    }

    public void printMoney(int money) {
        System.out.println("Money you have : " + money);
    }

    public void printAvailableAction(Pot pot) {
        System.out.print("Choose the Action : ");
        if (pot.getCurrentBet() > 0)
            System.out.println(Action.FOLD.toString() + " " + Action.BET + " " + Action.CALL);
        else
            System.out.println(Action.FOLD.toString() + " " + Action.BET + " " + Action.CHECK);
    }


    public void printForAction(Pot pot, Dealer dealer, Player player) {
        if (dealer.isAfterPreFlop())
            printBoard(dealer.getBoard());
        printPlayer(player);
        printPotSize(pot, player);
        printAvailableAction(pot);
    }

}
