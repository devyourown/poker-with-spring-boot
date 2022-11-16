package org.example.domain.game;

import org.example.domain.card.Card;
import org.example.domain.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private List<Card> board;
    private Dealer dealer;
    private GameStatus status;
    private Pot pot;
    private int lastTurnIndex;

    public Game(List<Player> players, int smallBlind, int bigBlind) {
        board = new ArrayList<>();
        this.players = new ArrayList<>(players);
        this.pot = new Pot(players, smallBlind, bigBlind);

        status = GameStatus.PRE_FLOP;
        initLastTurnIndex();
        this.dealer = new Dealer();
        distributeHands();
    }

    private void initLastTurnIndex() {
        lastTurnIndex = players.size() - 1;
    }

    private void distributeHands() {
        for (Player player : players) {
            player.setHands(dealer.handoutCards());
        }
    }

    public void setFlop() {
        status = GameStatus.FLOP;
        board.addAll(dealer.getFlopCards());
    }

    public void setTurn() {
        status = GameStatus.TURN;
        board.addAll(dealer.getTurnCard());
    }

    public void setRiver() {
        status = GameStatus.RIVER;
        board.addAll(dealer.getRiverCard());
    }

    public void playAction(int playerIndex, Action action, int betSize) throws Exception {
        Player player = players.get(playerIndex);
        if (action == Action.FOLD)
            actFold(player);
        else if (action == Action.CALL)
            actCall(player, playerIndex);
        else if (action == Action.CHECK)
            actCheck(playerIndex);
        else if (action == Action.BET || action == Action.RAISE) {
            actBet(player, betSize);
        }
    }

    private void actFold(Player player) {
        removePlayer(player);
        if (players.size() < 2)
            setEnd();
    }

    private void removePlayer(Player player) {
        players.remove(player);
    }

    private void actCall(Player player, int playerIndex) throws Exception {
        player.bet(pot.amountToCall(player));
        raisePotMoney(pot.amountToCall(player));
        if (isLastAction(playerIndex))
            setNextStatus();
    }


    private boolean isLastAction(int playerIndex) {
        if (playerIndex == lastTurnIndex)
            return true;
        return false;
    }

    private void actCheck(int playerIndex) {
        if (isLastAction(playerIndex))
            setNextStatus();
    }

    private void actBet(Player player, int betSize) throws Exception {
        pot.setCurrentBet(betSize);
        player.bet(betSize);
        raisePotMoney(betSize);
        pot.putPlayerBetLog(player, betSize);
        lastTurnIndex = players.indexOf(player);
    }

    private void raisePotMoney(int betSize) {
        pot.raiseMoney(betSize);
    }

    private void setNextStatus() {
        status = status.nextStatus();
    }

    private void setEnd() {
        status = GameStatus.END;
    }

    public boolean isEnd() {
        return status == GameStatus.END;
    }

    public int getPot() {
        return pot.getTotalAmount();
    }

    public GameStatus getStatus() {
        return status;
    }
}
