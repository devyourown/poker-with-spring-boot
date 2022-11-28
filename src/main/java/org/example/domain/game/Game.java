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

public class Game {
    private List<Player> players;
    private Dealer dealer;
    private GameStatus status;
    private Pot pot;
    private int lastTurnIndex;

    public Game(List<Player> players, int smallBlind, int bigBlind) {
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

    public void playAction(int playerIndex, Action action, int betSize) {
        Player player = players.get(playerIndex);
        if (action == Action.FOLD)
            actFold(player, playerIndex);
        else if (action == Action.CALL)
            actCall(player, playerIndex);
        else if (action == Action.CHECK)
            actCheck(playerIndex);
        else if (action == Action.BET)
            actBet(player, betSize);
    }

    private void actFold(Player player, int playerIndex) {
        setNextStatusWhenLastAction(playerIndex);
        if (lastTurnIndex != 0)
            lastTurnIndex--;
        players.remove(player);
        if (players.size() < 2)
            setEnd();
    }

    private void actCall(Player player, int playerIndex) {
        player.bet(pot.amountToCall(player));
        raisePotMoney(pot.amountToCall(player));
        setNextStatusWhenLastAction(playerIndex);
    }
    private void actCheck(int playerIndex) {
        setNextStatusWhenLastAction(playerIndex);
    }

    private void actBet(Player player, int betSize) {
        pot.setCurrentBet(betSize);
        player.bet(betSize);
        raisePotMoney(betSize);
        pot.putPlayerBetLog(player, betSize);
        resetLastTurnIndex(player);
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

    private void setNextStatusWhenLastAction(int playerIndex) {
        if (isLastAction(playerIndex)) {
            setNextStatus();
            initLastTurnIndex();
            dealer.setBoardAsStatus(status);
            setPlayersRanking(players, dealer.getBoard());
            pot.putZeroInBetLog(players);
            pot.resetCurrentBet();
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

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public int getPlayerMoneyOf(int index) {
        return players.get(index).getMoney();
    }

    public boolean hasThisPlayer(String playerId) {
        for (Player player : players) {
            if (player.getId().equals(playerId))
                return true;
        }
        return false;
    }
}
