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
    private Map<String, Player> playerMap = new HashMap();
    private Set<String> playerIdWhoHasRoom = new HashSet<>();

    @PostMapping("/status")
    public ResponseEntity<?> getRoomStatus(@RequestBody RoomDTO roomDTO) {
        Room room;
        try {
            room = roomService.getRoom(roomDTO.getRoomId());
        } catch (RoomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/ready")
    public ResponseEntity<?> readyPlayer(@AuthenticationPrincipal String playerId,
                                         @RequestBody RoomDTO roomDTO) {
        Room room;
        try {
            room = roomService.getRoom(roomDTO.getRoomId());
            playerMap.get(playerId).setStatus(Player.Status.READY);
            room.setPlayersToPlay();
        } catch (RoomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(room.getStatus());
    }


    @PostMapping("/new-room")
    public ResponseEntity<?> makeRoom(@AuthenticationPrincipal String playerId) {
        Room room = roomService.makeRoom();
        try {
            validatePlayerHasNoRoom(playerId);
            playerIdWhoHasRoom.add(playerId);
            Player player = playerMap.get(playerId);
            roomService.addPlayerToRoom(room.getId(), player);
        } catch (RoomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/manual-enter")
    public ResponseEntity<?> enterWithNumber(@AuthenticationPrincipal String playerId,
                                @RequestBody Map<String, String> roomId) {
        Room room;
        try {
            validatePlayerHasNoRoom(playerId);
            playerIdWhoHasRoom.add(playerId);
            room = roomService.getRoom(roomId.get("roomId"));
            Player player = playerMap.get(playerId);
            roomService.addPlayerToRoom(roomId.get("roomId"), player);
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
            playerIdWhoHasRoom.add(playerId);
            room = roomService.getAvailableRandomRoom();
            Player player = playerMap.get(playerId);
            roomService.addPlayerToRoom(room.getId(), player);
        } catch (RoomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/room-out")
    public void leaveRoom(@AuthenticationPrincipal String playerId) {
        playerIdWhoHasRoom.remove(playerId);
    }

    @PostMapping("/room-break")
    public ResponseEntity<?> removeRoom(@RequestBody Map<String, String> roomId) {
        try {
            removePlayerInRoom(roomService.getRoom(roomId.get("roomId")));
            roomService.removeRoom(roomId.get("roomId"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Success");
    }

    private void removePlayerInRoom(Room room) {
        for (Player player : room.getPlayers()) {
            playerIdWhoHasRoom.remove(player.getId());
        }
    }

    private RoomDTO getRoomDTO(final Room room) {
        return RoomDTO.builder()
                .roomId(room.getId())
                .players(getPlayerDTOs(room.getPlayers()))
                .status(room.getStatus())
                .build();
    }

    private List<PlayerDTO> getPlayerDTOs(final List<Player> players) {
        List<PlayerDTO> result = new ArrayList<>();
        for (Player player : players) {
            result.add(PlayerDTO.builder()
                    .id(player.getId())
                    .nickname(memberService.getById(player.getId()).getNickname())
                    .money(player.getMoney())
                    .build());
        }
        return result;
    }

    private void validatePlayerHasNoRoom(String playerId) throws RoomException {
        if (playerIdWhoHasRoom.contains(playerId))
            throw new RoomException(RoomException.ErrorCode.DUPLICATED_ROOM);
    }

    public void addPlayer(String playerId, Player player) {
        this.playerMap.put(playerId, player);
    }

}
