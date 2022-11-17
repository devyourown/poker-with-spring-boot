package org.example.domain.game;

import org.example.domain.card.Card;
import org.example.domain.player.Player;
import org.example.domain.rules.HandRanking;
import org.example.domain.rules.RankingSeperator;

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
        setNextStatusWhenLastAction(playerIndex);
    }

    private void actCheck(int playerIndex) {
        setNextStatusWhenLastAction(playerIndex);
    }

    private void setNextStatusWhenLastAction(int playerIndex) {
        if (isLastAction(playerIndex)) {
            setNextStatus();
            initLastTurnIndex();
            setBoardAsStatus();
        }
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
            makeWinner();
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

    private void makeWinner() {
        HandRanking winnerRanking = HandRanking.HIGH_CARD;
        Player winner = null;
        calculatePlayerRanking();
        for (Player player : players) {
            if (winnerRanking.ordinal() <= player.getRanking().ordinal()) {
                winnerRanking = player.getRanking();
                winner = player;
            }
        }
        if (isTiedGame(winnerRanking))
            splitMoney();
        else
            winner.raiseMoney(pot.getTotalAmount());
    }

    private void calculatePlayerRanking() {
        for (Player player : players) {
            List<Card> totalCards = new ArrayList<>(board);
            totalCards.addAll(player.getHands());
            player.setHandRanking(RankingSeperator.calculateCards(totalCards));
        }
    }

    private boolean isTiedGame(HandRanking winnerRanking) {
        List<Player> copiedPlayer = players.stream().toList();
        for (Player player : copiedPlayer) {
            if (player.getRanking() != winnerRanking)
                removePlayer(player);
        }
        if (players.size() > 1)
            return true;
        return false;
    }

    private void splitMoney() {
        int winnerPrize = pot.getTotalAmount() / players.size();
        if (pot.getTotalAmount() % players.size() != 0)
            winnerPrize += pot.getTotalAmount() / players.size();
        for (Player player : players) {
            player.raiseMoney(winnerPrize);
        }
    }

    private boolean isLastAction(int playerIndex) {
        if (playerIndex == lastTurnIndex)
            return true;
        return false;
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
