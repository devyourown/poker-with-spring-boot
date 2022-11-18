package org.example.console;

import org.example.domain.card.Card;

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
}
