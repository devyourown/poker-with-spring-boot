package org.poker.backend.controllers;

import org.poker.backend.dto.MemberDTO;
import org.poker.backend.persistence.entity.MemberEntity;
import org.poker.backend.security.TokenProvider;
import org.poker.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        MemberEntity memberEntity = memberService
                .getByCredentials(requestDTO.getEmail(), requestDTO.getPassword());
        if (memberEntity == null)
            throw new IllegalArgumentException("Login fail");
        final String token = tokenProvider.create(memberEntity);
        return ResponseEntity.ok(makeMemberDTOWithToken(memberEntity, token));
    }

    private MemberDTO makeMemberDTOWithToken(MemberEntity member, String token) {
        return MemberDTO.builder()
                .email(member.getEmail())
                .id(member.getId())
                .nickname(member.getNickname())
                .token(token)
                .money(member.getMoney())
                .build();
    }
}
