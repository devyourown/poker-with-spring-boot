package org.example.backend.controllers;

import org.example.backend.dto.RoomDTO;
import org.example.backend.persistence.entity.MemberEntity;
import org.example.backend.service.MemberService;
import org.example.backend.service.RoomService;
import org.example.domain.error.RoomException;
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
    @Autowired
    private RoomService roomService;

    @GetMapping("/status")
    public ResponseEntity<?> getRoomStatus(@RequestParam String roomId) {
        Room room;
        try {
            room = roomService.getRoom(roomId);
        } catch (RoomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(getRoomDTO(room));
    }


    @PostMapping("/new-room")
    public ResponseEntity<?> makeRoom(@AuthenticationPrincipal String playerId) {
        Room room = roomService.makeRoom();
        try {
            MemberEntity member = memberService.getById(playerId);
            roomService.addPlayerToRoom(room.getId(), member);
            memberService.create(member);
        } catch (RoomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/manual-enter")
    public ResponseEntity<?> enterWithNumber(@AuthenticationPrincipal String playerId,
                                @RequestParam String roomId) {
        Room room;
        try {
            room = roomService.getRoom(roomId);
            MemberEntity member = memberService.getById(playerId);
            roomService.addPlayerToRoom(roomId, member);
            memberService.create(member);
        } catch (RoomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/auto-enter")
    public ResponseEntity<?> enterRandomRoom(@AuthenticationPrincipal String playerId) {
        Room room = roomService.getAvailableRandomRoom();
        try {
            MemberEntity member = memberService.getById(playerId);
            roomService.addPlayerToRoom(room.getId(), member);
            memberService.create(member);
        } catch (RoomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/chat")
    public void chat() {}

    private RoomDTO getRoomDTO(final Room room) {
        return RoomDTO.builder()
                .players(room.getPlayers())
                .status(room.getStatus())
                .build();
    }

}
