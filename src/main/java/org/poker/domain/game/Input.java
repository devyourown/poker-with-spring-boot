package org.poker.domain.game;

import org.poker.console.UserAction;
import org.poker.domain.game.helper.Pot;
import org.poker.domain.player.Player;

public interface Input {
    UserAction getUserAction(Player player, Pot pot);
}
