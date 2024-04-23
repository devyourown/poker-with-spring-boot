package org.poker.domain.game.helper;

import org.poker.domain.player.Player;
import org.poker.domain.rules.Ranking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameResult {
    private final List<Player> winner;
    private final List<Player> players;

    public GameResult(List<Player> survivor, List<Player> wholePlayer, Pot pot) {
        survivor.sort(Comparator.comparingLong(Player::getRanks).reversed());
        this.winner = getWinners(survivor);
        pot.splitMoney(this.winner, getRest(survivor));
        this.players = wholePlayer;
    }

    private List<Player> getWinners(List<Player> players) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.getRanks() == players.get(0).getRanks())
                result.add(player);
            else
                break;
        }
        return result;
    }

    private List<Player> getRest(List<Player> players) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.getRanks() != players.get(0).getRanks())
                result.add(player);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int wholeMoney = 0;
        for (Player player : players) {
            Ranking rank = Ranking.getRank(player.getRanks());
            sb.append("ID: ").append(player.getId()).append(", money: ").append(player.getMoney())
                    .append(" with ").append(rank).append("\n");
            wholeMoney += player.getMoney();
        }
        sb.append("Whole Money : ").append(wholeMoney).append("\n");
        for (Player player : winner) {
            Ranking rank = Ranking.getRank(player.getRanks());
            sb.append("Winner : ").append(player.getId()).append(" with ").append(rank)
                    .append(" : ")
                    .append(player.getRanks()).append("\n");
        }
        return sb.toString();
    }
}
