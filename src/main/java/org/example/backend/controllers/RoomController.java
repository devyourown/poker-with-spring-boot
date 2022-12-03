package org.example.backend.controllers;

import org.example.backend.dto.PlayerDTO;
import org.example.backend.dto.RoomDTO;
import org.example.backend.security.UserAdapter;
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
    private final Map<String, Player> playerMap = new HashMap();
    private final Map<String, Room> playerRoomMap = new HashMap<>();

    @PostMapping("/status")
    public ResponseEntity<?> getRoomStatus(@RequestBody RoomDTO roomDTO) throws Exception {
        Room room = roomService.getRoom(roomDTO.getRoomId());
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/player-status")
    public ResponseEntity<?> readyPlayer(@AuthenticationPrincipal UserAdapter user,
                                         @RequestBody RoomDTO roomDTO) throws Exception {
        Room room = roomService.getRoom(roomDTO.getRoomId());
        playerMap.get(user.getUserId()).changeStatus();
        if (room.isReadToPlay())
            room.setPlayersToPlay();
        return ResponseEntity.ok().body(getRoomDTO(room));
    }


    @PostMapping("/new-room")
    public ResponseEntity<?> makeRoom(@AuthenticationPrincipal UserAdapter user) throws Exception {
        Room room = roomService.makeRoom();
        validatePlayerHasNoRoom(user.getUserId());
        Player player = playerMap.get(user.getUserId());
        roomService.addPlayerToRoom(room.getId(), player);
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/manual-enter")
    public ResponseEntity<?> enterWithNumber(@AuthenticationPrincipal String playerId,
                                @RequestBody RoomDTO roomDTO) throws Exception {
        validatePlayerHasNoRoom(playerId);
        Room room = roomService.getRoom(roomDTO.getRoomId());
        Player player = playerMap.get(playerId);
        playerRoomMap.put(player.getId(), room);
        roomService.addPlayerToRoom(roomDTO.getRoomId(), player);
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/auto-enter")
    public ResponseEntity<?> enterRandomRoom(@AuthenticationPrincipal UserAdapter user) throws Exception {
        validatePlayerHasNoRoom(user.getUserId());
        Room room = roomService.getAvailableRandomRoom();
        Player player = playerMap.get(user.getUserId());
        playerRoomMap.put(player.getId(), room);
        roomService.addPlayerToRoom(room.getId(), player);
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/room-out")
    public void leaveRoom(@AuthenticationPrincipal UserAdapter user) throws Exception {
        Room room = playerRoomMap.get(user.getUserId());
        room.removePlayer(playerMap.get(user.getUserId()));
        playerMap.remove(user.getUserId());
    }

    @PostMapping("/room-break")
    public ResponseEntity<?> removeRoom(@RequestBody RoomDTO roomDTO) throws Exception {
        removePlayerInRoom(roomService.getRoom(roomDTO.getRoomId()));
        roomService.removeRoom(roomDTO.getRoomId());
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/player-maker")
    public ResponseEntity<?> makePlayer(@AuthenticationPrincipal UserAdapter user) throws Exception{
        validatePlayerNotMade(user.getUserId());
        addPlayer(user.getUserId(), new Player(user.getUserId(), user.geUserMoney()));
        return ResponseEntity.ok("success");
    }

    private void removePlayerInRoom(Room room) {
        for (Player player : room.getPlayers()) {
            playerRoomMap.remove(player.getId());
            room.removePlayer(player);
            playerMap.remove(player.getId());
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

    private void validatePlayerNotMade(String playerId) throws Exception {
        if (playerMap.containsKey(playerId))
            throw new IllegalArgumentException("The Player is already made.");
    }

    private void validatePlayerHasNoRoom(String playerId) throws RoomException {
        if (playerRoomMap.containsKey(playerId))
            throw new RoomException(RoomException.ErrorCode.DUPLICATED_ROOM);
    }

    public void addPlayer(String playerId, Player player) {
        this.playerMap.put(playerId, player);
    }

}
