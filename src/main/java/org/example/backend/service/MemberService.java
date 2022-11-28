package org.example.backend.service;

import org.example.backend.persistence.MemberRepository;
import org.example.backend.persistence.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public MemberEntity getByCredentials(final String email, final String password,
                                         final PasswordEncoder encoder) {
        final MemberEntity original = memberRepository.findByEmail(email);
        if (original != null && encoder.matches(password, original.getPassword()))
            return original;
        return null;
    }

    public MemberEntity getById(final String id) {
        final Optional<MemberEntity> original = memberRepository.findById(id);
        if (original.isPresent()) {
            return original.get();
        }
        return null;
    }
}
