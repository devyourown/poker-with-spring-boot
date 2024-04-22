package org.example.domain.room;

import org.example.domain.error.RoomException;
import org.example.domain.game.Game;
import org.example.domain.player.Player;

import java.util.*;

public class Room {
    private static final int HEAD_COUNT_LIMIT = 10;
    private static final int PLAY_COUNT_LIMIT = 2;
    private List<Player> players;
    private String id;
    private Status status;

    public Room() {
        players = new ArrayList<>();
        status = Status.WAITING;
        id = UUID.randomUUID().toString();
    }

    public void setPlayersToPlay() throws RoomException {
        play();
    }

    public boolean isReadToPlay() {
        return isEveryoneReady() && canPlay();
    }

    private boolean isEveryoneReady() {
        for (Player player : players) {
            if (!player.isRoomReady())
                return false;
        }
        return true;
    }

    private void play() throws RoomException {
        validateToPlay();
        status = Status.PLAYING;
    }

    private void validateToPlay() throws RoomException {
        if (!canPlay())
            throw new RoomException(RoomException.ErrorCode.NOT_ENOUGH_PLAYER);
    }

    private boolean canPlay() {
        if (getNumOfPlayer() >= PLAY_COUNT_LIMIT)
            return true;
        return false;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    private boolean canAddPlayer() {
        if (getNumOfPlayer() < HEAD_COUNT_LIMIT
                && status == Status.WAITING)
            return true;
        return false;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public int getNumOfPlayer() {
        return players.size();
    }

    public Status getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public boolean isAvailableToEnter() {
        return canAddPlayer();
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public enum Status {
        WAITING,
        PLAYING;
    }
}
