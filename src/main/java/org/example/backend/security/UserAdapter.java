package org.example.backend.security;

import lombok.Getter;
import org.example.backend.persistence.entity.MemberEntity;

public class UserAdapter extends CustomUserDetails {
    private MemberEntity member;

    public UserAdapter(MemberEntity member) {
        super(member);
        this.member = member;
    }
}
