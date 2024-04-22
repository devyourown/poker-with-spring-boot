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

    private Map<String, Integer> userRequest = new HashMap<>();

    @PostMapping("/room-status")
    public ResponseEntity<?> getRoomStatus(@AuthenticationPrincipal UserAdapter user) throws Exception {
        if (userRequest.containsKey(user.getUserId())) {
            if (userRequest.get(user.getUserId()) > 1)
                return ResponseEntity.status(203).body("Already Sent");
        } else
            userRequest.put(user.getUserId(), 0);
        userRequest.put(user.getUserId(), userRequest.get(user.getUserId()) + 1);
        Room room = roomService.getRoomPlayerIn(user.getUserId());
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/player-status-change")
    public ResponseEntity<?> readyPlayer(@AuthenticationPrincipal UserAdapter user) throws Exception {
        Room room =  roomService.readyPlayer(user.getUserId());
        initRequestInRoom(room);
        return ResponseEntity.ok().body(getRoomDTO(room));
    }

    private void initRequestInRoom(Room room) {
        for (Player player : room.getPlayers()) {
            userRequest.put(player.getId(), 0);
        }
    }

    @GetMapping("/player-index")
    public int getPlayerIndex(@AuthenticationPrincipal UserAdapter user) {
        return roomService.getPlayerIndex(user.getUserId());
    }

    @PostMapping("/auto-enter")
    public ResponseEntity<?> enterRandomRoom(@AuthenticationPrincipal UserAdapter user) throws Exception {
        Room room = roomService.enterRandomRoom(user.getUserId());
        initRequestInRoom(room);
        return ResponseEntity.ok(getRoomDTO(room));
    }

    @PostMapping("/room-out")
    public void leaveRoom(@AuthenticationPrincipal UserAdapter user) throws Exception {
        roomService.removeRoom(user.getUserId());
    }

    @PostMapping("/player-maker")
    public ResponseEntity<?> makePlayer(@AuthenticationPrincipal UserAdapter user) throws Exception{
        roomService.makePlayer(user.getUserId(), user.geUserMoney());
        return ResponseEntity.ok("success");
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
                    .isReady(player.isRoomReady())
                    .build());
        }
        return result;
    }

}
