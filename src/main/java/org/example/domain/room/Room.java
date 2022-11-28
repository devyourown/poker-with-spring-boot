package org.example.domain.room;

import org.example.domain.error.RoomException;
import org.example.domain.game.Game;
import org.example.domain.player.Player;

import java.util.*;

public class Room {
    private static final int HEAD_COUNT_LIMIT = 10;
    private static final int PLAY_COUNT_LIMIT = 2;
    private Deque<Player> players;
    private String id;
    private Status status;

    public Room() {
        players = new ArrayDeque<>();
        status = Status.WAITING;
        id = UUID.randomUUID().toString();
    }

    public void changeOrder() {
        players.addFirst(players.getLast());
    }

    public void setPlayersToPlay() throws RoomException {
        play();
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players.stream().toList());
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

    public void addPlayer(Player player) throws RoomException {
        validateToAddPlayer();
        players.add(player);
    }

    private void validateToAddPlayer() throws RoomException {
        if (!canAddPlayer())
            throw new RoomException(RoomException.ErrorCode.TOO_MANY_PLAYER);
    }

    private boolean canAddPlayer() {
        if (getNumOfPlayer() < HEAD_COUNT_LIMIT)
            return true;
        return false;
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

    public enum Status {
        WAITING,
        PLAYING;
    }
}
