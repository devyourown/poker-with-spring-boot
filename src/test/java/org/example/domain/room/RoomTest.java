package org.example.domain.room;

import org.poker.domain.error.RoomException;
import org.poker.domain.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.poker.domain.room.Room;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    private Room room;

    @BeforeEach
    void makeRoom() {
        room = new Room();
    }

    @Test
    void testRoom() throws RoomException {
        room.addPlayer(new Player("1", 1000));
        assertEquals(1, room.getNumOfPlayer());
        assertEquals(Room.Status.WAITING, room.getStatus());
    }

    @Test
    void testPlayGameInRoom() throws RoomException {
        room.addPlayer(new Player("1", 1000));
        assertThrows(RoomException.class, () -> {
            room.setPlayersToPlay();
        });
        room.addPlayer(new Player("2", 1000));
        room.setPlayersToPlay();
        assertEquals(Room.Status.PLAYING, room.getStatus());
    }

    @Test
    void testChangeOrder() throws RoomException {
        Player player1 = new Player("1", 1000);
        Player player2 = new Player("2", 2000);
        room.addPlayer(player1);
        room.addPlayer(player2);
        room.setPlayersToPlay();
        assertTrue(room.getPlayers().get(0).equals(player1));
    }

    @Test
    void testRoomException() throws RoomException {
        for (int i=0; i<10; i++) {
            room.addPlayer(new Player("1", 1000));
        }
        assertFalse(room.isAvailableToEnter());
    }
}