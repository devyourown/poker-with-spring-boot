package org.example.domain.game.helper;

import org.example.domain.player.Player;
import org.example.domain.player.PlayerTable;
import org.example.domain.rules.Ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameResult {
    private List<Player> winner;
    private Presenter presenter;
    private List<Player> players;

    public GameResult(List<Player> players, Pot pot) {
        this.players = players;
        presenter = new Presenter(pot);
        winner = makeWinner(this.players);
        if (isAllInPlayerWin(this.players)) {
            giveMoneyWhenAllIn(this.players);
            return ;
        }
        presenter.giveMoney(winner);
    }

    private boolean isAllInPlayerWin(List<Player> players) {
        return !getAllInWinners(players).isEmpty();
    }

    private List<Player> getAllInWinners(List<Player> players) {
        List<Player> result = new ArrayList<>();
        Ranking winnerRanking = getWinnerRanking(players);
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
        Ranking winnerRanking = getWinnerRanking(players);
        return getPlayersWithSameRank(players, winnerRanking);
    }

    private Ranking getWinnerRanking(List<Player> players) {
        Ranking result = Ranking.HIGH_CARD;
        for (Player player : players) {
            if (result.ordinal() <= player.getRanking().ordinal()) {
                result = player.getRanking();
            }
        }
        return result;
    }

    private List<Player> getPlayersWithSameRank(List<Player> players, Ranking rank) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Player player : players) {
            sb.append("ID: " + player.getId() + " money: " + player.getMoney() + "\n");
        }
        for (Player player : winner) {
            sb.append("Winner : " + player.getId() + "\n");
        }
        return sb.toString();
    }
}
