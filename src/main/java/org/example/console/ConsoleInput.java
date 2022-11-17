package org.example.console;

import org.example.domain.game.Action;

import java.util.Scanner;

public class ConsoleInput {
    private static Scanner scanner = new Scanner(System.in);
    public static UserAction getUserAction() {
        return new UserAction(getAction(), getBetSize());
    }

    private static Action getAction() {
        String action = scanner.next();
        validateActionInput(action);
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
        String betSize = scanner.next();
        validateBetInput(betSize);
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
