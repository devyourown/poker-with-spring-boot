package org.example.backend.service;

import org.example.backend.persistence.entity.MemberEntity;
import org.example.domain.error.RoomException;
import org.example.domain.player.Player;
import org.example.domain.room.Room;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class RoomService {

    private HashMap<String, Room> roomHashMap = new HashMap<>();

    public Room getRoom(String roomId) throws RoomException {
        validateRoomIsOpen(roomId);
        if (roomHashMap.containsKey(roomId))
            return roomHashMap.get(roomId);
        return null;
    }

    public Room getAvailableRandomRoom() {
        for (Room room : roomHashMap.values()) {
            if (room.isAvailableToEnter()) {
                return room;
            }
        }
        return makeRoom();
    }

    public Room makeRoom() {
        Room room = new Room();
        roomHashMap.put(room.getId(), room);
        return room;
    }

    public void addPlayerToRoom(String roomId, MemberEntity member) throws RoomException {
        validateRoomIsOpen(roomId);
        validateMemberNoRoom(member);
        Room room = roomHashMap.get(roomId);
        room.addPlayer(new Player(member.getId(), member.getMoney()));
    }

    private void validateRoomIsOpen(String roomId) throws RoomException {
        if (!roomHashMap.containsKey(roomId))
            throw new RoomException(RoomException.ErrorCode.ID_NOT_EXIST);
        if (!roomHashMap.get(roomId).isAvailableToEnter())
            throw new RoomException(RoomException.ErrorCode.TOO_MANY_PLAYER);
    }

    private void validateMemberNoRoom(MemberEntity member) throws RoomException {
        if (member.isHasRoom())
            throw new RoomException(RoomException.ErrorCode.DUPLICATED_ROOM);
    }
}
