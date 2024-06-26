package org.poker.backend.service;

import org.poker.console.ConsoleInput;
import org.poker.console.ConsoleOutput;
import org.poker.domain.deck.RandomDeck;
import org.poker.domain.error.RoomException;
import org.poker.domain.game.Game;
import org.poker.domain.player.Player;
import org.poker.domain.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomService {
    @Autowired
    private MemberService memberService;

    private HashMap<String, Room> occupiedRooms = new HashMap<>();
    private final Map<String, Player> playerIdMap = new HashMap();
    private final Map<String, Room> playerIdRoomMap = new HashMap<>();

    public Game makeGame(String roomId) throws RoomException {
        validateRoomExist(roomId);
        validateRoomCanPlay(roomId);
        Room room = occupiedRooms.get(roomId);
        return new Game(room.getPlayers(), 100, 200, new RandomDeck(room.getNumOfPlayer()),
                new ConsoleInput(new Scanner(System.in)),
                new ConsoleOutput());
    }

    private void validateRoomCanPlay(String roomId) throws RoomException {
        if (occupiedRooms.get(roomId).getPlayers().size() <= 1)
            throw new RoomException(RoomException.ErrorCode.NOT_ENOUGH_PLAYER);
        if (occupiedRooms.get(roomId).getStatus() == Room.Status.WAITING)
            throw new RoomException(RoomException.ErrorCode.NOT_PLAYING);
    }

    public Room readyPlayer(String playerId) throws Exception {
        Room room = playerIdRoomMap.get(playerId);
        if (room.isReadToPlay())
            room.setPlayersToPlay();
        return room;
    }

    public Room getRoomPlayerIn(String playerId) throws Exception {
        validatePlayerExist(playerId);
        return playerIdRoomMap.get(playerId);
    }

    public Room enterRandomRoom(String playerId) throws Exception {
        validatePlayerExist(playerId);
        if (playerIdRoomMap.containsKey(playerId))
            return playerIdRoomMap.get(playerId);
        Room room = getAvailableRandomRoom();
        Player player = playerIdMap.get(playerId);
        playerIdRoomMap.put(player.getId(), room);
        addPlayerToRoom(room.getId(), player);
        return room;
    }

    private void validatePlayerExist(String playerId) throws Exception {
        if (!playerIdMap.containsKey(playerId))
            throw new IllegalArgumentException("Player is not existed.");
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

    public int getPlayerIndex(String playerId) {
        Room room = playerIdRoomMap.get(playerId);
        List<Player> players = room.getPlayers();
        for (Player player : players) {
            if (player.getId().equals(playerId))
                return players.indexOf(player);
        }
        return -1;
    }

    public void removeRoom(String playerId) throws RoomException {
        Room room = playerIdRoomMap.get(playerId);
        validateRoomCanBeRemoved(room);
        room.removePlayer(playerIdMap.get(playerId));
        playerIdMap.remove(playerId);
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
        if (playerIdMap.containsKey(playerId))
            return ;
        addPlayer(playerId, new Player(playerId, playerMoney));
    }

    private void addPlayer(String playerId, Player player) {
        this.playerIdMap.put(playerId, player);
    }
}
