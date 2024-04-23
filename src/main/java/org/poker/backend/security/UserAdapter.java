package org.poker.backend.security;

import org.poker.backend.persistence.entity.MemberEntity;

public class UserAdapter extends CustomUserDetails {
    private MemberEntity member;

    public UserAdapter(MemberEntity member) {
        super(member);
        this.member = member;
    }
}
