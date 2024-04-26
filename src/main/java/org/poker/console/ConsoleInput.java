package org.poker.console;

import org.poker.domain.error.BetException;
import org.poker.domain.game.Action;
import org.poker.domain.game.Input;
import org.poker.domain.game.helper.Pot;
import org.poker.domain.player.Player;

import java.util.*;

public class ConsoleInput implements Input {
    private final Scanner scanner;
    private final Set<String> idSet;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
        this.idSet = new HashSet<>();
    }

    public ArrayList<Player> initUserFromInput() {
        ArrayList<Player> result = new ArrayList<>();
        int numOfPlayers = getNumberFromInput("How Many Players: ");
        for (int i=0; i<numOfPlayers; i++) {
            result.add(getPlayerFromInput());
        }
        return result;
    }

    private int getNumberFromInput(String inputText) {
        System.out.print(inputText);
        String numOfPlayers = scanner.next();
        if (!isNumber(numOfPlayers))
            return getNumberFromInput(inputText);
        return Integer.parseInt(numOfPlayers);
    }

    private Player getPlayerFromInput() {
        String id = getIdFromInput();
        int moneyAmount = getNumberFromInput("How much money the player has : ");
        return new Player(id, moneyAmount);
    }

    private String getIdFromInput() {
        System.out.print("Current Player ID : ");
        String id = scanner.next();
        if (!isPossibleId(id)) {
            return getIdFromInput();
        }
        idSet.add(id);
        return id;
    }

    private boolean isPossibleId(String id) {
        if (idSet.contains(id)) {
            System.out.println("[ERROR] ID Should be unique.");
            return false;
        }
        return true;
    }

    private boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
        } catch (Exception e) {
            System.out.println("[ERROR] Input should be number.");
            return false;
        }
        return true;
    }

    public boolean askPlayAgain() {
        System.out.print("Do you want to stop game?(Y/N): ");
        String answer = scanner.next();
        return !answer.equals("N") && !answer.equals("n");
    }

    public UserAction getUserAction(Player player, Pot pot) {
        Action action = getAction(pot.getCurrentBet());
        if (shouldGetMoney(action))
            return new UserAction(action, getBetSize(player, pot.getCurrentBet()));
        return new UserAction(action, 0);
    }

    private boolean shouldGetMoney(Action action) {
        return action == Action.BET;
    }

    private Action getAction(int betSize) {
        System.out.print("ACTION : ");
        String action = scanner.next();
        try {
            validateActionInput(action, betSize);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return getAction(betSize);
        }
        return Action.valueOf(action);
    }

    private void validateActionInput(String input, int betSize) {
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
        else {
            if (isCall(action))
                throw new IllegalArgumentException("[ERROR] The Action is not available.");
        }
    }

    private boolean isCheck(Action action) {
        return action == Action.CHECK;
    }

    private boolean isCall(Action action) {
        return action == Action.CALL;
    }

    private int getBetSize(Player player, int prevBetSize) {
        System.out.print("Betting : ");
        String betSize = scanner.next();
        try {
            validateBetInput(player, betSize, prevBetSize);
        } catch (BetException e) {
            System.out.println(e.errorMessage());
            return getBetSize(player, prevBetSize);
        }
        return Integer.parseInt(betSize);
    }

    private static void validateBetInput(Player player, String input, int prevBetSize) throws BetException {
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
        if (player.getMoney() < betSize)
            throw new BetException(BetException.ErrorCode.MONEY_NOT_ENOUGH);
    }
}
