package org.example.backend.controllers;

import org.example.backend.dto.PlayerDTO;
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

import java.util.*;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private RoomService roomService;
    private Set<String> playerIdSet = new HashSet<>();

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
            validatePlayerHasNoRoom(playerId);
            playerIdSet.add(playerId);
            MemberEntity member = memberService.getById(playerId);
            roomService.addPlayerToRoom(room.getId(), member);
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
            validatePlayerHasNoRoom(playerId);
            playerIdSet.add(playerId);
            room = roomService.getRoom(roomId);
            MemberEntity member = memberService.getById(playerId);
            roomService.addPlayerToRoom(roomId, member);
        } catch (RoomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/auto-enter")
    public ResponseEntity<?> enterRandomRoom(@AuthenticationPrincipal String playerId) {
        Room room;
        try {
            validatePlayerHasNoRoom(playerId);
            playerIdSet.add(playerId);
            room = roomService.getAvailableRandomRoom();
            MemberEntity member = memberService.getById(playerId);
            roomService.addPlayerToRoom(room.getId(), member);
        } catch (RoomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/chat")
    public void chat() {}

    @PostMapping("/room-break")
    public ResponseEntity<?> removeRoom(@RequestParam String roomId) {
        try {
            roomService.removeRoom(roomId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Success");
    }

    private RoomDTO getRoomDTO(final Room room) {
        return RoomDTO.builder()
                .players(getPlayerDTOs(room.getPlayers()))
                .status(room.getStatus())
                .build();
    }

    private List<PlayerDTO> getPlayerDTOs(final List<Player> players) {
        List<PlayerDTO> result = new ArrayList<>();
        for (Player player : players) {
            result.add(PlayerDTO.builder()
                    .id(player.getId())
                    .nickname(memberService
                            .getById(player.getId()).getNickname())
                    .money(player.getMoney())
                    .build());
        }
        return result;
    }

    private void validatePlayerHasNoRoom(String playerId) throws RoomException {
        if (playerIdSet.contains(playerId))
            throw new RoomException(RoomException.ErrorCode.DUPLICATED_ROOM);
    }

}
