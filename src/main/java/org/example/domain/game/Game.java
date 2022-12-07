package org.example.domain.game;

import org.example.domain.card.Card;
import org.example.domain.game.helper.Dealer;
import org.example.domain.game.helper.GameResult;
import org.example.domain.game.helper.Pot;
import org.example.domain.player.Player;
import org.example.domain.rules.RankingCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Game {
    private List<Player> players;
    private Dealer dealer;
    private GameStatus status;
    private Pot pot;
    private int lastTurnIndex;
    private int currentTurnIndex;
    private final String gameId;
    private Action lastAction;
    private int lastActionIndex;

    public Game(List<Player> players, int smallBlind, int bigBlind) {
        this.gameId = UUID.randomUUID().toString();
        this.players = new ArrayList<>(players);
        this.pot = new Pot(players, smallBlind, bigBlind);

        status = GameStatus.PRE_FLOP;
        initLastTurnIndex();
        this.currentTurnIndex = 0;
        this.lastActionIndex = -1;
        this.lastAction = null;

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

    public void playAction(Action action, int betSize) {
        Player player = players.get(currentTurnIndex);
        this.lastAction = action;
        this.lastActionIndex = currentTurnIndex;
        if (action == Action.FOLD)
            actFold(player);
        else if (action == Action.CALL)
            actCall(player);
        else if (action == Action.CHECK)
            actCheck();
        else if (action == Action.BET)
            actBet(player, betSize);
        initCurrentTurnWhenOver();
    }

    private void initCurrentTurnWhenOver() {
        if (currentTurnIndex == players.size())
            currentTurnIndex = 0;
    }

    private void actFold(Player player) {
        setNextStatusWhenLastAction();
        if (lastTurnIndex != 0)
            lastTurnIndex--;
        if (currentTurnIndex != 0)
            currentTurnIndex--;
        players.remove(player);
        if (players.size() < 2)
            setEnd();
    }

    private void actCall(Player player) {
        player.bet(pot.amountToCall(player));
        raisePotMoney(pot.amountToCall(player));
        setNextStatusWhenLastAction();
    }
    private void actCheck() {
        setNextStatusWhenLastAction();
    }

    private void actBet(Player player, int betSize) {
        pot.setCurrentBet(betSize);
        player.bet(betSize);
        raisePotMoney(betSize);
        pot.putPlayerBetLog(player, betSize);
        resetLastTurnIndex(player);
        currentTurnIndex++;
    }

    private void raisePotMoney(int betSize) {
        pot.raiseMoney(betSize);
    }

    private void resetLastTurnIndex(Player player) {
        if (players.indexOf(player) == 0)
            initLastTurnIndex();
        else
            lastTurnIndex = players.indexOf(player) - 1;
    }

    private void setNextStatusWhenLastAction() {
        if (isLastAction()) {
            setNextStatus();
            initLastTurnIndex();
            currentTurnIndex = 0;
            dealer.setBoardAsStatus(status);
            setPlayersRanking(players, dealer.getBoard());
            pot.putZeroInBetLog(players);
            pot.resetCurrentBet();
            return ;
        }
        currentTurnIndex++;
    }

    private boolean isLastAction() {
        if (currentTurnIndex == lastTurnIndex)
            return true;
        return false;
    }

    private void setNextStatus() {
        status = status.nextStatus();
    }

    private void setPlayersRanking(List<Player> players, List<Card> cards) {
        for (Player player : players) {
            List<Card> totalCards = new ArrayList<>(cards);
            totalCards.addAll(player.getHands());
            player.setHandRanking(RankingCalculator.calculateCards(totalCards));
        }
    }

    private void setEnd() {
        new GameResult(players, pot);
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

    public List<Card> getBoard() {
        return dealer.getBoard();
    }

    public int getBettingSize() {
        return pot.getCurrentBet();
    }

    public List<Card> getPlayerHandsOf(int index) {
        return players.get(index).getHands();
    }

    public int getSizeOfPlayers() {
        return players.size();
    }

    public int getPlayerMoneyOf(int index) {
        return players.get(index).getMoney();
    }

    public List<Card> getHandsOf(String playerId) {
        for (Player player : players) {
            if (player.getId().equals(playerId))
                return player.getHands();
        }
        return Collections.EMPTY_LIST;
    }

    public boolean isCurrentTurn(String playerId) {
        return currentTurnIndex == getIndexOf(playerId);
    }

    private int getIndexOf(String playerId) {
        for (int i=0; i<players.size(); i++) {
            if (players.get(i).getId().equals(playerId))
                return i;
        }
        return -1;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void removePlayer(String playerId) {
        for (Player player : players) {
            if (player.getId().equals(playerId)) {
                players.remove(player);
                if (players.size() < 2)
                    setEnd();
            }
        }
    }

    public int currentTurnIndex() {
        return this.currentTurnIndex;
    }

    public Action getLastAction() {
        return this.lastAction;
    }

    public int getLastActionIndex() {
        return this.lastActionIndex;
    }
}
