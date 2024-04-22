package org.example.domain.game;

import org.example.console.ConsoleInput;
import org.example.console.ConsoleOutput;
import org.example.console.UserAction;
import org.example.domain.card.Card;
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

    public Game(List<Player> players, int smallBlind, int bigBlind) {
        this.gameId = UUID.randomUUID().toString();
        this.players = new ArrayList<>(players);
        this.playerTable = new PlayerTable(players);
        this.pot = new Pot(players, smallBlind, bigBlind);
        status = GameStatus.PRE_FLOP;
        this.dealer = new Dealer(players.size());
        distributeHands();
    }

    public GameResult play() {
        for (GameStatus gameStatus : GameStatus.values()) {
            playUntilTurnOver();
            if (isEnd())
                break;
        }
        List<Player> lastPlayers = convertTableToList(playerTable);
        setPlayersRanking(lastPlayers, dealer.getBoard());
        return new GameResult(lastPlayers, pot);
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
        boolean isStart = true;
        int numOfResponseToTurnOver = playerTable.getSize();
        List<Player> foldPlayers = new ArrayList<>();
        while (!turnOver(numOfResponseToTurnOver)) {
            ConsoleOutput.printForAction(this, pot);
            UserAction userAction = ConsoleInput.getUserAction(playerTable.getCurrentPlayer(),
                    pot.getCurrentBet(), isStart);
            if (userAction.action == Action.BET)
                numOfResponseToTurnOver = playerTable.getSize();
            if (userAction.action == Action.FOLD)
                foldPlayers.add(playerTable.getCurrentPlayer());
            playAction(userAction.action, userAction.betSize);
            playerTable.moveNext();
            isStart = false;
            numOfResponseToTurnOver--;
        }
        dealer.setBoard();
        pot.refresh(foldPlayers);
    }

    private boolean turnOver(int leftNumOfResponse) {
        if (isEnd()) return true;
        return leftNumOfResponse <= 0;
    }

    private void distributeHands() {
        for (Player player : players) {
            player.setHands(dealer.handoutCards());
        }
    }

    public void resetGame() {
        this.playerTable.changeOrder();
        this.dealer.reset();
        status = GameStatus.PRE_FLOP;
        distributeHands();
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
        playerTable.getCurrentPlayer().setPlayingStatus(Player.PlayingStatus.CALL);
    }

    private void actBet(int betSize) {
        pot.bet(playerTable.getCurrentPlayer(), betSize);
        playerTable.getCurrentPlayer().setPlayingStatus(Player.PlayingStatus.BET);
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

    public List<Card> getBoard() {
        return dealer.getBoard();
    }

    public int getSizeOfPlayers() {
        return players.size();
    }

    public String getGameId() {
        return this.gameId;
    }

    public int getCurrentPlayerMoney() {
        return playerTable.getCurrentPlayer().getMoney();
    }

    public List<Card> getCurrentPlayerHands() {
        return playerTable.getCurrentPlayer().getHands();
    }
}
