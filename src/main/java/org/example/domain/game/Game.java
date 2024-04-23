package org.example.domain.game;

import org.example.console.ConsoleInput;
import org.example.console.ConsoleOutput;
import org.example.console.UserAction;
import org.example.domain.card.Card;
import org.example.domain.deck.Deck;
import org.example.domain.game.helper.Dealer;
import org.example.domain.game.helper.GameResult;
import org.example.domain.game.helper.Pot;
import org.example.domain.player.Player;
import org.example.domain.player.PlayerTable;
import org.example.domain.rules.RankingCalculator;

import java.util.*;

public class Game {
    private final List<Player> players;
    private final PlayerTable playerTable;
    private final Dealer dealer;
    private GameStatus status;
    private final Pot pot;
    private final String gameId;

    public Game(List<Player> players, int smallBlind, int bigBlind, Deck deck) {
        this.gameId = UUID.randomUUID().toString();
        this.players = new ArrayList<>(players);
        this.playerTable = new PlayerTable(players);
        this.pot = new Pot(players, smallBlind, bigBlind);
        status = GameStatus.PRE_FLOP;
        this.dealer = new Dealer(players, deck);
    }

    public GameResult play() {
        for (GameStatus gameStatus : GameStatus.values()) {
            if (gameStatus == GameStatus.END || isEnd())
                break;
            playUntilTurnOver();
        }
        List<Player> lastPlayers = convertTableToList(playerTable);
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
        int numOfResponseToTurnOver = playerTable.getSize();
        List<Player> foldPlayers = new ArrayList<>();
        while (!turnOver(numOfResponseToTurnOver)) {
            ConsoleOutput.printForAction(pot, dealer, playerTable.getCurrentPlayer());
            UserAction userAction = ConsoleInput.getUserAction(
                    playerTable.getCurrentPlayer(),
                    pot.getCurrentBet());
            if (userAction.action == Action.BET)
                numOfResponseToTurnOver = playerTable.getSize();
            if (userAction.action == Action.FOLD)
                foldPlayers.add(playerTable.getCurrentPlayer());
            playAction(userAction.action, userAction.betSize);
            playerTable.moveNext();
            numOfResponseToTurnOver--;
        }
        dealer.nextStatus();
        pot.refresh(foldPlayers);
    }

    private boolean turnOver(int leftNumOfResponse) {
        if (isEnd()) return true;
        return leftNumOfResponse <= 0;
    }

    public void resetGame() {
        removeNoMoneyPlayer();
        this.playerTable.reset(players);
        this.dealer.reset(players);
        this.pot.reset(convertTableToList(playerTable));
        status = GameStatus.PRE_FLOP;
    }

    private void removeNoMoneyPlayer() {
        players.removeIf(player -> player.getMoney() < pot.getBigBlind());
    }

    public void playAction(Action action, int betSize) {
        if (action == Action.FOLD)
            actFold();
        else if (action == Action.CALL)
            actCall();
        else if (action == Action.BET)
            actBet(betSize);
    }

    private void actFold() {
        playerTable.removeSelf();
        if (impossibleToPlay())
            setEnd();
    }

    private boolean impossibleToPlay() {
        return playerTable.getSize() < 2;
    }

    private void actCall() {
        pot.call(playerTable.getCurrentPlayer());
    }

    private void actBet(int betSize) {
        pot.bet(playerTable.getCurrentPlayer(), betSize);
    }

    private void setEnd() {
        status = GameStatus.END;
    }

    private void setPlayersRanking(List<Player> players, List<Card> cards) {
        for (Player player : players) {
            List<Card> totalCards = new ArrayList<>(cards);
            totalCards.addAll(player.getHands());
            player.setRanks(RankingCalculator.calculateCards(totalCards));
        }
    }

    public boolean isEnd() {
        return status == GameStatus.END;
    }

    public String getGameId() {
        return this.gameId;
    }
}
