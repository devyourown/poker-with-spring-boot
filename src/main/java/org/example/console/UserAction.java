package org.example.console;

import org.example.domain.game.Action;

public class UserAction {
    public Action action;
    public int betSize;

    public UserAction(Action action, int betSize) {
        this.action = action;
        this.betSize = betSize;
    }
}
