package org.example.domain.game;

import org.example.domain.card.Card;
import org.example.domain.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private List<Integer> bettingLog;
    private List<Card> board;
    private int pot;
    private Dealer dealer;
    private int smallAmount;
    private int bigAmount;
    private int currentBet;
    private GameStatus status;

    public Game(List<Player> players, int smallAmount, int bigAmount) {
        board = new ArrayList<>();
        this.players = new ArrayList<>(players);
        this.smallAmount = smallAmount;
        this.bigAmount = bigAmount;

        this.currentBet = bigAmount;
        this.pot = smallAmount + bigAmount;

        status = GameStatus.PRE_FLOP;

        this.dealer = new Dealer();
        distributeHands();
        initBettingLog();
    }

    private void distributeHands() {
        for (Player player : players) {
            player.setHands(dealer.handout());
        }
    }

    private void initBettingLog() {
        bettingLog = new ArrayList<>();
        for (int i=0; i<players.size()-2; i++)
            bettingLog.add(0);
        bettingLog.add(smallAmount);
        bettingLog.add(bigAmount);
    }

    public void setFlop() {
        status = GameStatus.FLOP;
        board.addAll(dealer.flop());
    }

    public void setTurn() {
        status = GameStatus.TURN;
        board.addAll(dealer.turn());
    }

    public void setRiver() {
        status = GameStatus.RIVER;
        board.addAll(dealer.river());
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

    private void actCall(Player player, int playerIndex) throws Exception {
        player.bet(amountToCall(player));
        raisePotMoney(amountToCall(player));
        if (isLastAction(playerIndex))
            setEnd();
    }

    private boolean isLastAction(int playerIndex) {
        if (status == GameStatus.RIVER && playerIndex == players.size() - 1)
            return true;
        return false;
    }

    private int amountToCall(Player player) {
        return currentBet - bettingLog.get(players.indexOf(player));
    }

    private void actCheck(int playerIndex) {
        if (isLastAction(playerIndex))
            setEnd();
    }

    private void removePlayer(Player player) {
        players.remove(player);
    }

    private void actBet(Player player, int betSize) throws Exception {
        setCurrentBet(betSize);
        player.bet(betSize);
        raisePotMoney(betSize);
        bettingLog.set(players.indexOf(player), betSize);
    }

    private void raisePotMoney(int betSize) {
        this.pot += betSize;
    }

    private void setCurrentBet(int betSize) {
        validateBetSize(betSize);
        this.currentBet = betSize;
    }

    private void validateBetSize(int betSize) {
        if (currentBet > betSize)
            throw new IllegalArgumentException("[ERROR] betSize is small than prevBet");
    }

    private void setEnd() {
        status = GameStatus.END;
    }

    public int getPot() {
        return this.pot;
    }

    public int getNumOfPlayers() {
        return players.size();
    }
}
