package org.example.backend.service;

import org.example.backend.persistence.MemberRepository;
import org.example.backend.persistence.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public MemberEntity create(final MemberEntity entity) {
        validate(entity);
        return memberRepository.save(entity);
    }

    private void validate(MemberEntity entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity is empty.");
        else if (entity.getEmail() == null)
            throw new IllegalArgumentException("Email is null.");
        else if (memberRepository.existsByEmail(entity.getEmail()))
            throw new IllegalArgumentException("Email already exists.");
    }

    public MemberEntity getByCredentials(final String email, final String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }
}
