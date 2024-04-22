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

    public GameResult(List<Player> players, Pot pot) {
        this.players = players;
        players.sort(Comparator.comparingInt(Player::getRanks).reversed());
        this.winner = getWinners(players);
        pot.splitMoney(this.winner, getRest(players));
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
            sb.append("ID: " + player.getId() + " money: " + player.getMoney() + "\n");
        }
        for (Player player : winner) {
            sb.append("Winner : " + player.getId() + "\n");
        }
        return sb.toString();
    }
}
