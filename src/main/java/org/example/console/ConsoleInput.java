package org.example.console;

import org.example.domain.game.Action;

import java.util.Scanner;

public class ConsoleInput {
    private static Scanner scanner = new Scanner(System.in);
    public static UserAction getUserAction() {
        Action action = getAction();
        int betSize = getBetSize();
        return new UserAction(action, betSize);
    }

    private static Action getAction() {
        return Action.valueOf("check");
    }

    private static int getBetSize() {
        return 1;
    }
}
