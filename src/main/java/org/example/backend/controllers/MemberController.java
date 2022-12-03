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
        MemberEntity entity = MemberEntity.builder()
                .email(requestDTO.getEmail())
                .nickname(requestDTO.getNickname())
                .password(requestDTO.getPassword())
                .money(10000)
                .build();
        MemberEntity registeredMember = memberService.create(entity);
        MemberDTO responseDTO = MemberDTO.builder()
                .id(registeredMember.getId())
                .email(registeredMember.getEmail())
                .nickname(registeredMember.getNickname())
                .money(registeredMember.getMoney())
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody MemberDTO requestDTO) {
        MemberEntity entity = memberService
                .getByCredentials(requestDTO.getEmail(), requestDTO.getPassword());

        if (entity == null)
            return ResponseEntity.badRequest().body("login failed.");
        final String token = tokenProvider.create(entity);
        final MemberDTO responseDTO = MemberDTO.builder()
                .email(entity.getEmail())
                .id(entity.getId())
                .nickname(entity.getNickname())
                .token(token)
                .money(entity.getMoney())
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/status")
    public ResponseEntity<?> verifyToken(@AuthenticationPrincipal String id) {
        if (id != null)
            return ResponseEntity.ok().body("Verified");
        return ResponseEntity.badRequest().body("Not verified");
    }
}
