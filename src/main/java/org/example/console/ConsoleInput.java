package org.example.console;

import org.example.domain.game.Action;

import java.util.Scanner;

public class ConsoleInput {
    private static Scanner scanner = new Scanner(System.in);
    public static UserAction getUserAction() {
        Action action = getAction();
        if (shouldGetMoney(action))
            return new UserAction(action, getBetSize());
        return new UserAction(action, 0);
    }

    private static boolean shouldGetMoney(Action action) {
        return Action.RAISE == action || Action.BET == action;
    }

    private static Action getAction() {
        System.out.print("ACTION : ");
        String action = scanner.next();
        try {
            validateActionInput(action);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return getAction();
        }
        return Action.valueOf(action);
    }

    private static void validateActionInput(String action) {
        try {
            Action.valueOf(action);
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 액션을 제대로 입력 해주세요.");
        }
    }

    private static int getBetSize() {
        System.out.print("Betting : ");
        String betSize = scanner.next();
        try {
            validateBetInput(betSize);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return getBetSize();
        }
        return Integer.parseInt(betSize);
    }

    private static void validateBetInput(String betSize) {
        try {
            Integer.parseInt(betSize);
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 값을 제대로 입력해 주세요.");
        }
    }
}
