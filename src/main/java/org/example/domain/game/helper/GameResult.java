package org.example.domain.game.helper;

import org.example.domain.player.Player;
import org.example.domain.player.PlayerTable;
import org.example.domain.rules.Ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameResult {
    private List<Player> winner;
    private List<Player> players;

    public GameResult(List<Player> survivor, List<Player> wholePlayer, Pot pot) {
        survivor.sort(Comparator.comparingInt(Player::getRanks).reversed());
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
        for (Player player : players) {
            Ranking rank = Ranking.getRank(player.getRanks());
            sb.append("ID: ").append(player.getId()).append(", money: ").append(player.getMoney())
                    .append(" with ").append(rank).append("\n");
        }
        for (Player player : winner) {
            Ranking rank = Ranking.getRank(player.getRanks());
            sb.append("Winner : ").append(player.getId()).append(" with ").append(rank)
                    .append(" : ")
                    .append(player.getRanks()).append("\n");
        }
        return sb.toString();
    }
}
