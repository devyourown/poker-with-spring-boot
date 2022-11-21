package org.example.domain.room;

public class RoomIdMaker {
    private static int roomId = 1;

    public static int makeRoomId() {
        return roomId++;
    }
}
