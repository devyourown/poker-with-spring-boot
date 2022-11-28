package org.example.backend.controllers;

import org.example.backend.dto.MemberDTO;
import org.example.backend.persistence.entity.MemberEntity;
import org.example.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/signup")
    public void createMember(@RequestBody MemberDTO dto) {
        
    }
}
