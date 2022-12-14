package org.example.backend.controllers;

import org.example.backend.dto.MemberDTO;
import org.example.backend.persistence.entity.MemberEntity;
import org.example.backend.security.TokenProvider;
import org.example.backend.service.MemberService;
import org.example.domain.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> createMember(@RequestBody MemberDTO requestDTO) {
        return ResponseEntity.ok().body(memberService.createMember(requestDTO));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody MemberDTO requestDTO) {
        return ResponseEntity.ok(memberService
                    .authenticate(requestDTO.getEmail(), requestDTO.getPassword()));
    }
}
