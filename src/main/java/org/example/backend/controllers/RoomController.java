package org.example.backend.controllers;

import org.example.backend.persistence.entity.MemberEntity;
import org.example.backend.service.MemberService;
import org.example.domain.player.Player;
import org.example.domain.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/new-room")
    public String makeRoom(@AuthenticationPrincipal String playerId) {
        Room room = new Room();
        try {
            MemberEntity member = memberService.getById(playerId);
            room.addPlayer(new Player(playerId, member.getMoney()));
        } catch (Exception e) {
            ResponseEntity.badRequest().body(e.getMessage());
        }
        return room.getId();
    }
}
