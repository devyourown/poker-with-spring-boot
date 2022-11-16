package org.example.domain.game;

import org.example.domain.card.Card;
import org.example.domain.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private List<Card> board;
    private int pot;
    private Dealer dealer;
    private int smallAmount;
    private int bigAmount;
    private int currentBet;

    public Game(List<Player> players, int smallAmount, int bigAmount) {
        board = new ArrayList<>();
        this.players = players;
        this.smallAmount = smallAmount;
        this.bigAmount = bigAmount;
        pot = 0;

        this.dealer = new Dealer();
        distributeHands();
    }

    private void distributeHands() {
        for (Player player : players) {
            player.setHands(dealer.handout());
        }
    }

    public void setFlop() {
        board.addAll(dealer.flop());
    }

    public void setTurn() {
        board.addAll(dealer.turn());
    }

    public void setRiver() {
        board.addAll(dealer.river());
    }

    public void playAction(int order, Action action, int betSize) throws Exception {
        Player player = players.get(order);
        if (action == Action.FOLD)
            removePlayer(order);
        else if (action == Action.CALL)
            player.bet(currentBet);
        else if (action == Action.CHECK)
            return ;
        else if (action == Action.BET) {
            player.bet(betSize);
            currentBet = betSize;
        }
        else if (action == Action.RAISE) {
            player.bet(betSize);
            currentBet = betSize;
        }
    }

    private void actBetting(Player player, int betSize) {
        this.currentBet = betSize;
    }

    private void removePlayer(int order) {
        players.remove(order);
    }
}
