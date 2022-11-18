package org.example.domain.game;

import org.example.domain.card.Card;
import org.example.domain.player.Player;
import org.example.domain.rules.HandRanking;
import org.example.domain.rules.RankingCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameResult {
    private List<Player> winner;
    private List<Player> players;
    private List<Player> allInPlayers;
    private Pot pot;

    public GameResult(List<Player> players, Pot pot) {
        this.players = players;
        this.pot = pot;
        winner = makeWinner();
        giveMoney(winner);
    }

    private boolean hasAllInPlayer(List<Player> players) {
        return true;
    }

    private List<Player> makeWinner() {
        HandRanking winnerRanking = getWinnerRanking();
        return getPlayersWithSameRank(winnerRanking);
    }

    private HandRanking getWinnerRanking() {
        HandRanking result = HandRanking.HIGH_CARD;
        for (Player player : players) {
            if (result.ordinal() <= player.getRanking().ordinal()) {
                result = player.getRanking();
            }
        }
        return result;
    }

    private List<Player> getPlayersWithSameRank(HandRanking rank) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.getRanking() == rank)
                result.add(player);
        }
        return result;
    }

    private void giveMoney(List<Player> winners) {
        if (isTiedGame(winners)) {
            for (Player player : winners)
                player.raiseMoney(getWinnerPrize(winners));
        }
        else
            winners.get(0).raiseMoney(pot.getTotalAmount());
    }

    private boolean isTiedGame(List<Player> winners) {
        if (winners.size() > 1)
            return true;
        return false;
    }

    private int getWinnerPrize(List<Player> winners) {
        int winnerPrize = pot.getTotalAmount() / winners.size();
        if (pot.getTotalAmount() % winners.size() != 0)
            winnerPrize += pot.getTotalAmount() / winners.size();
        return winnerPrize;
    }

    public boolean hasWinner(Player player) {
        return winner.contains(player);
    }

    public static void setPlayersRanking(List<Player> players, List<Card> cards) {
        for (Player player : players) {
            List<Card> totalCards = new ArrayList<>(cards);
            totalCards.addAll(player.getHands());
            player.setHandRanking(RankingCalculator.calculateCards(totalCards));
        }
    }
}
