package org.poker.backend.service;

import org.poker.backend.dto.MemberDTO;
import org.poker.backend.persistence.MemberRepository;
import org.poker.backend.persistence.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public MemberDTO createMember(MemberDTO memberDTO) {
        MemberEntity entity = MemberEntity.builder()
                .email(memberDTO.getEmail())
                .nickname(memberDTO.getNickname())
                .password(memberDTO.getPassword())
                .money(10000)
                .build();
        MemberEntity registeredMember = create(entity);
        return makeMemberDTO(registeredMember);
    }

    private MemberEntity create(final MemberEntity entity) {
        validate(entity);
        entity.setPassword(encoder.encode(entity.getPassword()));
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

    private MemberDTO makeMemberDTO(MemberEntity memberEntity) {
        return MemberDTO.builder()
                .id(memberEntity.getId())
                .email(memberEntity.getEmail())
                .nickname(memberEntity.getNickname())
                .money(memberEntity.getMoney())
                .build();
    }

    public MemberEntity getByCredentials(final String email, final String password) {
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
