package org.example.domain.room;

import org.example.domain.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void testRoom() {
        Player player = new Player(1000);
        Room room = new Room();
        room.addPlayer(player);
        assertEquals(1, room.getNumOfPlayer());
    }
}