package org.poker.domain.game;

import org.poker.domain.game.helper.Dealer;
import org.poker.domain.game.helper.Pot;
import org.poker.domain.player.Player;

public interface Output {
    void printForAction(Pot pot, Dealer dealer, Player player);
}
