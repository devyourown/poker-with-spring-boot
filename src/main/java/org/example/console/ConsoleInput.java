package org.example.console;

import org.example.domain.error.BetException;
import org.example.domain.game.Action;

import java.util.Scanner;

public class ConsoleInput {
    private static Scanner scanner = new Scanner(System.in);
    public static UserAction getUserAction(int betSize, int gameOrder) {
        Action action = getAction(betSize, gameOrder);
        if (shouldGetMoney(action))
            return new UserAction(action, getBetSize(betSize));
        return new UserAction(action, 0);
    }

    private static boolean shouldGetMoney(Action action) {
        return action == Action.BET;
    }

    private static Action getAction(int betSize, int gameOrder) {
        System.out.print("ACTION : ");
        String action = scanner.next();
        try {
            validateActionInput(action, betSize, gameOrder);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return getAction(betSize, gameOrder);
        }
        return Action.valueOf(action);
    }

    private static void validateActionInput(String input, int betSize, int gameOrder) {
        Action action;
        try {
            action = Action.valueOf(input);
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 액션을 제대로 입력해 주세요.");
        }
        if (betSize > 0) {
            if (isCheck(action))
                throw new IllegalArgumentException("[ERROR] 가능한 액션만 입력해 주세요.");
        }
        else if (gameOrder == 0) {
            if (isCall(action))
                throw new IllegalArgumentException("[ERROR] 가능한 액션만 입력해 주세요.");
        }
        else {
            if (isCall(action))
                throw new IllegalArgumentException("[ERROR] 가능한 액션만 입력해 주세요.");
        }
    }

    private static boolean isCheck(Action action) {
        return action == Action.CHECK || action == Action.BET;
    }

    private static boolean isCall(Action action) {
        return action == Action.CALL;
    }

    private static int getBetSize(int prevBetSize) {
        System.out.print("Betting : ");
        String betSize = scanner.next();
        try {
            validateBetInput(betSize, prevBetSize);
        } catch (BetException e) {
            System.out.println(e.errorMessage());
            return getBetSize(prevBetSize);
        }
        return Integer.parseInt(betSize);
    }

    private static void validateBetInput(String input, int prevBetSize) throws BetException {
        int betSize;
        try {
            betSize = Integer.parseInt(input);
        } catch (Exception e) {
            throw new BetException(BetException.ErrorCode.NOT_INTEGER);
        }
        if (betSize <= prevBetSize)
            throw new BetException(BetException.ErrorCode.TOO_SMALL_BET_SIZE);
        if (betSize % 100 != 0)
            throw new BetException(BetException.ErrorCode.INVALID_BET_SIZE);
    }
}
