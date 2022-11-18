package org.example.domain.game;

import org.example.domain.card.Card;
import org.example.domain.player.Player;
import org.example.domain.rules.HandRanking;
import org.example.domain.rules.RankingSeparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private List<Player> players;
    private List<Card> board;
    private Dealer dealer;
    private GameStatus status;
    private Pot pot;
    private int lastTurnIndex;
    private GameResult result;

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

    public void playAction(int playerIndex, Action action, int betSize) throws Exception {
        Player player = players.get(playerIndex);
        if (action == Action.FOLD)
            actFold(player, playerIndex);
        else if (action == Action.CALL)
            actCall(player, playerIndex);
        else if (action == Action.CHECK)
            actCheck(playerIndex);
        else if (action == Action.BET || action == Action.RAISE) {
            actBet(player, betSize);
        }
    }

    private void actFold(Player player, int playerIndex) {
        setNextStatusWhenLastAction(playerIndex);
        if (lastTurnIndex == players.size() - 1)
            lastTurnIndex--;
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
        setNextStatusWhenLastAction(playerIndex);
    }

    private void raisePotMoney(int betSize) {
        pot.raiseMoney(betSize);
    }

    private void setNextStatusWhenLastAction(int playerIndex) {
        if (isLastAction(playerIndex)) {
            setNextStatus();
            initLastTurnIndex();
            setBoardAsStatus();
            calculatePlayerRanking();
            pot.putZeroInBetLog(players);
        }
    }

    private boolean isLastAction(int playerIndex) {
        if (playerIndex == lastTurnIndex)
            return true;
        return false;
    }

    private void setNextStatus() {
        status = status.nextStatus();
    }

    private void setBoardAsStatus() {
        if (getStatus() == GameStatus.FLOP)
            setFlop();
        else if (getStatus() == GameStatus.TURN)
            setTurn();
        else if (getStatus() == GameStatus.RIVER)
            setRiver();
        else if (getStatus() == GameStatus.END)
            result = new GameResult(players, pot);
    }

    private void calculatePlayerRanking() {
        for (Player player : players) {
            List<Card> totalCards = new ArrayList<>(board);
            totalCards.addAll(player.getHands());
            player.setHandRanking(RankingSeparator.calculateCards(totalCards));
        }
    }

    private void actCheck(int playerIndex) {
        setNextStatusWhenLastAction(playerIndex);
    }

    private void setFlop() {
        board.addAll(dealer.getFlopCards());
    }

    private void setTurn() {
        board.addAll(dealer.getTurnCard());
    }

    private void setRiver() {
        board.addAll(dealer.getRiverCard());
    }

    private void actBet(Player player, int betSize) throws Exception {
        pot.setCurrentBet(betSize);
        player.bet(betSize);
        raisePotMoney(betSize);
        pot.putPlayerBetLog(player, betSize);
        lastTurnIndex = players.indexOf(player);
    }

    private void setEnd() {
        status = GameStatus.END;
        setBoardAsStatus();
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

    public List<Card> getBoard() {
        return Collections.unmodifiableList(this.board);
    }

    public int getBettingSize() {
        return pot.getCurrentBet();
    }

    public Player getPlayerOf(int index) {
        return players.get(index);
    }

    public int getSizeOfPlayers() {
        return players.size();
    }

    public boolean isWinner(Player player) {
        if (result.getWinner().contains(player))
            return true;
        return false;
    }
}
