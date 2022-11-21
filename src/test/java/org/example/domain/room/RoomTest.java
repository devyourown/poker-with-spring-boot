package org.example.domain.room;

import org.example.domain.error.RoomException;
import org.example.domain.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    private Room room;

    @BeforeEach
    void makeRoom() {
        room = new Room();
    }

    @Test
    void testRoom() throws RoomException {
        room.addPlayer(new Player(1000));
        assertEquals(1, room.getNumOfPlayer());
        assertEquals(Room.Status.WAITING, room.getStatus());
        assertEquals(1, room.getId());
    }

    @Test
    void testPlayGameInRoom() throws RoomException {
        room.addPlayer(new Player(1000));
        assertThrows(RoomException.class, () -> {
            room.getPlayersToPlay();
        });
        room.addPlayer(new Player(1000));
        room.getPlayersToPlay();
        assertEquals(Room.Status.PLAYING, room.getStatus());
    }

    @Test
    void testChangeOrder() throws RoomException {

    }

    @Test
    void testRoomException() throws RoomException {
        for (int i=0; i<10; i++) {
            room.addPlayer(new Player(1000));
        }
        assertThrows(RoomException.class, () -> {
            room.addPlayer(new Player(1000));
        });
    }
}