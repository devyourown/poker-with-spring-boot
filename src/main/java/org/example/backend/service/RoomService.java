package org.example.backend.service;

import org.example.domain.error.RoomException;
import org.example.domain.game.Game;
import org.example.domain.player.Player;
import org.example.domain.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomService {
    @Autowired
    private MemberService memberService;

    private HashMap<String, Room> occupiedRooms = new HashMap<>();
    private final Map<String, Player> playerMap = new HashMap();
    private final Map<String, Room> playerRoomMap = new HashMap<>();

    public Game makeGame(String roomId) throws RoomException {
        validateRoomExist(roomId);
        validateRoomCanPlay(roomId);
        Room room = occupiedRooms.get(roomId);
        return new Game(room.getPlayers(), 100, 200);
    }

    private void validateRoomCanPlay(String roomId) throws RoomException {
        if (occupiedRooms.get(roomId).getPlayers().size() <= 1)
            throw new RoomException(RoomException.ErrorCode.NOT_ENOUGH_PLAYER);
        if (occupiedRooms.get(roomId).getStatus() == Room.Status.WAITING)
            throw new RoomException(RoomException.ErrorCode.NOT_PLAYING);
    }

    public Room readyPlayer(String playerId) throws Exception {
        Room room = playerRoomMap.get(playerId);
        playerMap.get(playerId).changeStatus();
        if (room.isReadToPlay())
            room.setPlayersToPlay();
        return room;
    }

    public Room getRoomPlayerIn(String playerId) throws Exception {
        validatePlayerExist(playerId);
        return playerRoomMap.get(playerId);
    }

    public Room enterRandomRoom(String playerId) throws Exception {
        validatePlayerExist(playerId);
        validatePlayerHasNoRoom(playerId);
        Room room = getAvailableRandomRoom();
        Player player = playerMap.get(playerId);
        playerRoomMap.put(player.getId(), room);
        addPlayerToRoom(room.getId(), player);
        return room;
    }

    private void validatePlayerExist(String playerId) throws Exception {
        if (!playerMap.containsKey(playerId))
            throw new IllegalArgumentException("Player is not existed.");
    }

    private void validatePlayerHasNoRoom(String playerId) throws RoomException {
        if (playerRoomMap.containsKey(playerId))
            throw new RoomException(RoomException.ErrorCode.DUPLICATED_ROOM);
    }

    private Room getAvailableRandomRoom() {
        for (Room room : occupiedRooms.values()) {
            if (room.isAvailableToEnter()) {
                return room;
            }
        }
        return makeRoom();
    }

    private Room makeRoom() {
        Room room = new Room();
        occupiedRooms.put(room.getId(), room);
        return room;
    }

    private void addPlayerToRoom(String roomId, Player player) throws RoomException {
        validateRoomIsOpen(roomId);
        Room room = occupiedRooms.get(roomId);
        room.addPlayer(player);
    }

    public void removeRoom(String playerId) throws RoomException {
        Room room = playerRoomMap.get(playerId);
        validateRoomCanBeRemoved(room);
        room.removePlayer(playerMap.get(playerId));
        playerMap.remove(playerId);
        occupiedRooms.remove(room);
    }

    private void validateRoomCanBeRemoved(Room room) throws RoomException {
        if (room.getStatus() == Room.Status.PLAYING)
            throw new RoomException(RoomException.ErrorCode.NOT_REMOVABLE);
        if (room.getNumOfPlayer() > 0)
            throw new RoomException(RoomException.ErrorCode.NOT_REMOVABLE);
    }

    private void validateRoomExist(String roomId) throws RoomException {
        if (!occupiedRooms.containsKey(roomId))
            throw new RoomException(RoomException.ErrorCode.ID_NOT_EXIST);
    }

    private void validateRoomIsOpen(String roomId) throws RoomException {
        if (!occupiedRooms.get(roomId).isAvailableToEnter())
            throw new RoomException(RoomException.ErrorCode.TOO_MANY_PLAYER);
    }

    public void makePlayer(String playerId, int playerMoney) throws Exception {
        validatePlayerNotMade(playerId);
        addPlayer(playerId, new Player(playerId, playerMoney));
    }

    private void validatePlayerNotMade(String playerId) throws Exception {
        if (playerMap.containsKey(playerId))
            throw new IllegalArgumentException("The Player is already made.");
    }

    private void addPlayer(String playerId, Player player) {
        this.playerMap.put(playerId, player);
    }
}
