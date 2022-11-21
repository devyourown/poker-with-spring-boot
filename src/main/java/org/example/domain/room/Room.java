package org.example.domain.room;

import org.example.domain.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private List<Player> players;
    private long roomId;

    public Room() {
        players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public int getNumOfPlayer() {
        return players.size();
    }
}
