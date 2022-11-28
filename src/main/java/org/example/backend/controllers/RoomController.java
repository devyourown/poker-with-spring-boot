package org.example.backend.controllers;

import org.example.backend.dto.RoomDTO;
import org.example.backend.persistence.entity.MemberEntity;
import org.example.backend.service.MemberService;
import org.example.domain.player.Player;
import org.example.domain.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private MemberService memberService;

    private HashMap<String, Room> roomHashMap = new HashMap<>();

    @GetMapping("/status")
    public ResponseEntity<?> getRoomStatus(@RequestParam String roomId) {
        Room room = roomHashMap.get(roomId);
        RoomDTO roomDTO = RoomDTO.builder()
                .players(room.getPlayers())
                .status(room.getStatus())
                .build();
        return ResponseEntity.ok(roomDTO);
    }


    @PostMapping("/new-room")
    public ResponseEntity<?> makeRoom(@AuthenticationPrincipal String playerId) {
        MemberEntity member = memberService.getById(playerId);
        if (member.isHasRoom())
            ResponseEntity.badRequest().body("error : has room");
        Room room = new Room();
        room.addPlayer(new Player(playerId, member.getMoney()));
        RoomDTO roomDTO = RoomDTO.builder()
                .players(room.getPlayers())
                .status(room.getStatus())
                .build();
        return ResponseEntity.ok(roomDTO);
    }

    @PostMapping("/manual-enter")
    public ResponseEntity<?> enterWithNumber(@AuthenticationPrincipal String playerId,
                                @RequestParam String roomId) {
        MemberEntity member = memberService.getById(playerId);
        if (member.isHasRoom())
            ResponseEntity.badRequest().body("error : has room");
        Room room = roomHashMap.get(roomId);
        if (!room.isAvailableToEnter())
            ResponseEntity.badRequest().body("error : room is currently closed");
        room.addPlayer(new Player(playerId, member.getMoney()));
        RoomDTO roomDTO = RoomDTO.builder()
                .players(room.getPlayers())
                .status(room.getStatus())
                .build();
        return ResponseEntity.ok(roomDTO);
    }

    @PostMapping("/auto-enter")
    public ResponseEntity<?> enterRandomRoom(@AuthenticationPrincipal String playerId) {
        MemberEntity member = memberService.getById(playerId);
        if (member.isHasRoom())
            ResponseEntity.badRequest().body("error : has room");
        for (Room room : roomHashMap.values()) {
            if (room.isAvailableToEnter()) {
                room.addPlayer(new Player(playerId, member.getMoney()));
                RoomDTO roomDTO = RoomDTO.builder()
                        .players(room.getPlayers())
                        .status(room.getStatus())
                        .build();
                return ResponseEntity.ok(roomDTO);
            }
        }
        return makeRoom(playerId);
    }

    @PostMapping("/chat")
    public void chat() {}

}
