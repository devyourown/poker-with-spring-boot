package org.example.backend.controllers;

import org.example.backend.dto.MemberDTO;
import org.example.backend.persistence.entity.MemberEntity;
import org.example.backend.security.TokenProvider;
import org.example.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                .money(requestDTO.getMoney())
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
}
