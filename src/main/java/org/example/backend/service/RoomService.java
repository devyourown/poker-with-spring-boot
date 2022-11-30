package org.example.backend.service;

import org.example.backend.persistence.entity.MemberEntity;
import org.example.domain.error.RoomException;
import org.example.domain.player.Player;
import org.example.domain.room.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
public class RoomService {

    private HashMap<String, Room> occupiedRooms = new HashMap<>();

    public Room getRoom(String roomId) throws RoomException {
        validateRoomIsOpen(roomId);
        if (occupiedRooms.containsKey(roomId))
            return occupiedRooms.get(roomId);
        return null;
    }

    public Room getAvailableRandomRoom() {
        for (Room room : occupiedRooms.values()) {
            if (room.isAvailableToEnter()) {
                return room;
            }
        }
        return makeRoom();
    }

    public Room makeRoom() {
        Room room = new Room();
        occupiedRooms.put(room.getId(), room);
        return room;
    }

    public void removeRoom(String roomId) throws RoomException {
        validateRoomCanBeRemoved(roomId);
        occupiedRooms.remove(roomId);
    }

    private void validateRoomCanBeRemoved(String roomId) throws RoomException {
        Room room = occupiedRooms.get(roomId);
        if (room.getStatus() == Room.Status.PLAYING)
            throw new RoomException(RoomException.ErrorCode.NOT_REMOVABLE);
        if (room.getNumOfPlayer() > 0)
            throw new RoomException(RoomException.ErrorCode.NOT_REMOVABLE);
    }

    public void addPlayerToRoom(String roomId, MemberEntity member) throws RoomException {
        validateRoomIsOpen(roomId);
        Room room = occupiedRooms.get(roomId);
        room.addPlayer(new Player(member.getId(), member.getMoney()));
    }

    private void validateRoomIsOpen(String roomId) throws RoomException {
        if (!occupiedRooms.containsKey(roomId))
            throw new RoomException(RoomException.ErrorCode.ID_NOT_EXIST);
        if (!occupiedRooms.get(roomId).isAvailableToEnter())
            throw new RoomException(RoomException.ErrorCode.TOO_MANY_PLAYER);
    }
}
