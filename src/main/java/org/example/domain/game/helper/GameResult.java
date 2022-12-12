package org.example.domain.game.helper;

import org.example.domain.player.Player;
import org.example.domain.rules.HandRanking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameResult {
    private List<Player> winner;
    private Presenter presenter;

    public GameResult(List<Player> players, Pot pot) {
        presenter = new Presenter(pot);
        winner = makeWinner(players);
        if (isAllInPlayerWin(players)) {
            giveMoneyWhenAllIn(players);
            return ;
        }
        presenter.giveMoney(winner);
    }

    private boolean isAllInPlayerWin(List<Player> players) {
        return !getAllInWinners(players).isEmpty();
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

    private void giveMoneyWhenAllIn(List<Player> players) {
        List<Player> losers = makePeopleWhoGetPaidButLose(players);
        presenter.giveMoneyToAllInWinner(winner, losers);
        if (!losers.isEmpty())
            presenter.giveMoney(makeWinner(losers));
    }

    private List<Player> makePeopleWhoGetPaidButLose(List<Player> players) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (!winner.contains(player))
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

    public List<Player> getWinner() {
        return Collections.unmodifiableList(winner);
    }
}
