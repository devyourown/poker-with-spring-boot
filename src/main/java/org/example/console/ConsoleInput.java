package org.example.console;

import org.example.domain.error.BetException;
import org.example.domain.game.Action;
import org.example.domain.player.Player;

import java.util.*;

public class ConsoleInput {
    private static Scanner scanner = new Scanner(System.in);
    private static Set<String> idSet = new HashSet<>();

    public static ArrayList<Player> initUserFromInput() {
        ArrayList<Player> result = new ArrayList<>();
        int numOfPlayers = getNumberFromInput("How Many Players: ");
        for (int i=0; i<numOfPlayers; i++) {
            result.add(getPlayerFromInput());
        }
        return result;
    }

    private static int getNumberFromInput(String inputText) {
        while (true) {
            System.out.print(inputText);
            String numOfPlayers = scanner.next();
            if (!isNumber(numOfPlayers))
                continue;
            return Integer.parseInt(numOfPlayers);
        }
    }

    private static Player getPlayerFromInput() {
        String id = getIdFromInput();
        int moneyAmount = getNumberFromInput("How much money the player has : ");
        return new Player(id, moneyAmount);
    }

    private static String getIdFromInput() {
        while (true) {
            System.out.print("Current Player ID : ");
            String id = scanner.next();
            if (idSet.contains(id))
                continue;
            idSet.add(id);
            return id;
        }
    }

    private static boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
        } catch (Exception e) {
            System.out.println("[ERROR] Input should be number.");
            return false;
        }
        return true;
    }

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
            throw new IllegalArgumentException("[ERROR] The Action is not available.");
        }
        if (betSize > 0) {
            if (isCheck(action))
                throw new IllegalArgumentException("[ERROR] The Action is impossible.");
        }
        else if (gameOrder == 0) {
            if (isCall(action))
                throw new IllegalArgumentException("[ERROR] The Action is impossible.");
        }
        else {
            if (isCall(action))
                throw new IllegalArgumentException("[ERROR] The Action is not available.");
        }
    }

    private static boolean isCheck(Action action) {
        return action == Action.CHECK;
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
