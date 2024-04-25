package org.poker.domain.game;

import org.poker.console.ConsoleInput;
import org.poker.console.ConsoleOutput;
import org.poker.console.UserAction;
import org.poker.domain.card.Card;
import org.poker.domain.deck.Deck;
import org.poker.domain.game.helper.Dealer;
import org.poker.domain.game.helper.GameResult;
import org.poker.domain.game.helper.Pot;
import org.poker.domain.player.Player;
import org.poker.domain.player.PlayerTable;
import org.poker.domain.rules.RankingCalculator;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final List<Player> players;
    private final PlayerTable playerTable;
    private final List<Player> foldPlayers;
    private final Dealer dealer;
    private final Pot pot;
    private final String gameId;
    private final List<Player> allinPlayers;
    private int leftNumOfResponse;

    public Game(List<Player> players, int smallBlind, int bigBlind, Deck deck) {
        this.gameId = UUID.randomUUID().toString();
        this.players = new ArrayList<>(players);
        this.playerTable = new PlayerTable(players);
        this.pot = new Pot(players, smallBlind, bigBlind);
        this.dealer = new Dealer(players, deck);
        this.foldPlayers = new ArrayList<>();
        this.allinPlayers = new ArrayList<>();
    }

    public GameResult play() {
        for (GameStatus gameStatus : GameStatus.values()) {
            if (gameStatus == GameStatus.END || isEnd())
                break;
            leftNumOfResponse = playerTable.getSize();
            playUntilTurnOver();
        }
        dealer.showDown();
        List<Player> lastPlayers = convertTableToList(playerTable);
        lastPlayers.addAll(allinPlayers);
        lastPlayers = lastPlayers.stream().distinct().collect(Collectors.toList());
        setPlayersRanking(lastPlayers, dealer.getBoard());
        return new GameResult(lastPlayers, players, pot);
    }

    private List<Player> convertTableToList(PlayerTable playerTable) {
        List<Player> result = new ArrayList<>();
        result.add(playerTable.getCurrentPlayer());
        playerTable.moveNext();
        Player player = playerTable.getCurrentPlayer();
        while (result.get(0) != player) {
            result.add(player);
            playerTable.moveNext();
            player = playerTable.getCurrentPlayer();
        }
        return result;
    }

    private void playUntilTurnOver() {
        while (!isTurnOver() && !isAllPlayerAllIn()) {
            ConsoleOutput.printForAction(pot, dealer, playerTable.getCurrentPlayer());
            UserAction userAction = ConsoleInput.getUserAction(playerTable.getCurrentPlayer(), pot);
            playAction(userAction.action, userAction.betSize);
        }
        dealer.nextStatus();
        pot.refresh(foldPlayers);
        foldPlayers.clear();
    }

    private boolean isTurnOver() {
        return leftNumOfResponse <= 0;
    }

    public void resetGame() {
        removeNoMoneyPlayer();
        this.playerTable.reset(players);
        this.dealer.reset(players);
        this.pot.reset(convertTableToList(playerTable));
        this.allinPlayers.clear();
        players.forEach(Player::gameOver);
    }

    private void removeNoMoneyPlayer() {
        players.removeIf(player -> player.getMoney() < pot.getBigBlind());
    }

    private void playAction(Action action, int betSize) {
        if (action == Action.FOLD)
            actFold();
        else if (action == Action.CALL)
            actCall();
        else if (action == Action.BET)
            actBet(betSize);
        if (playerTable.getCurrentPlayer().hasAllin()) {
            allinPlayers.add(playerTable.getCurrentPlayer());
            playerTable.removeSelf();
        }
        playerTable.moveNext();
        leftNumOfResponse--;
    }

    private void actFold() {
        foldPlayers.add(playerTable.getCurrentPlayer());
        playerTable.removeSelf();
    }

    private void actCall() { pot.call(playerTable.getCurrentPlayer()); }

    private void actBet(int betSize) {
        pot.bet(playerTable.getCurrentPlayer(), betSize);
        leftNumOfResponse = playerTable.getSize();
    }

    private boolean isAllPlayerAllIn() {
        return playerTable.getSize() == 0;
    }

    private void setPlayersRanking(List<Player> players, List<Card> cards) {
        for (Player player : players) {
            List<Card> totalCards = new ArrayList<>(cards);
            totalCards.addAll(player.getHands());
            player.setRanks(RankingCalculator.calculateCards(totalCards));
        }
    }

    public boolean isEnd() {
        return playerTable.getSize() < 2;
    }

    public String getGameId() {
        return this.gameId;
    }
}
