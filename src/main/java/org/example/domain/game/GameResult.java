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
    private Pot pot;

    public GameResult(List<Player> players, Pot pot) {
        this.pot = pot;
        if (isAllInPlayerWin(players)) {
            giveMoneyToLosers(makePeopleWhoGetPaidButLose(players));
            
        } else {
            winner = makeWinner(players);
            giveMoney(winner);
        }
    }

    private void giveMoneyToLosers(List<Player> losers) {
        if (losers.isEmpty())
            return ;
        giveMoney(makeWinner(losers));
    }

    private boolean isAllInPlayerWin(List<Player> players) {
        return !getAllInWinners(players).isEmpty();
    }

    private List<Player> makePeopleWhoGetPaidButLose(List<Player> players) {
        List<Player> result = new ArrayList<>();
        int highestBetInAllIn = getHighestAllIn(players);
        for (Player player : players) {
            if (pot.getPlayerBetLog(player) > highestBetInAllIn)
                result.add(player);
        }
        return result;
    }

    private int getHighestAllIn(List<Player> players) {
        int result = 0;
        for (Player player : getAllInWinners(players)) {
            if (result < pot.getPlayerBetLog(player))
                result = pot.getPlayerBetLog(player);
        }
        return result;
    }

    private List<Player> getAllInWinners(List<Player> players) {
        List<Player> result = new ArrayList<>();
        HandRanking winnerRanking = getWinnerRanking(players);
        for (Player player : players) {
            if (player.isAllIn() && player.getRanking() == winnerRanking)
                result.add(player);
        }
        return result;
    }

    private List<Player> makeWinner(List<Player> players) {
        HandRanking winnerRanking = getWinnerRanking(players);
        return getPlayersWithSameRank(players, winnerRanking);
    }

    private HandRanking getWinnerRanking(List<Player> players) {
        HandRanking result = HandRanking.HIGH_CARD;
        for (Player player : players) {
            if (result.ordinal() <= player.getRanking().ordinal()) {
                result = player.getRanking();
            }
        }
        return result;
    }

    private List<Player> getPlayersWithSameRank(List<Player> players, HandRanking rank) {
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
