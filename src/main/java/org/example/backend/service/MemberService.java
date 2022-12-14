package org.example.backend.service;

import org.example.backend.dto.MemberDTO;
import org.example.backend.persistence.MemberRepository;
import org.example.backend.persistence.entity.MemberEntity;
import org.example.backend.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

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

    public MemberDTO authenticate(final String email, final String password) {
        MemberEntity memberEntity = getByCredentials(email, password);
        if (memberEntity == null)
            throw new IllegalArgumentException("Login fail");
        final String token = tokenProvider.create(memberEntity);
        return makeMemberDTOWithToken(memberEntity, token);
    }

    private MemberEntity getByCredentials(final String email, final String password) {
        final MemberEntity original = memberRepository.findByEmail(email);
        if (original != null && encoder.matches(password, original.getPassword()))
            return original;
        return null;
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

    public MemberEntity getById(final String id) {
        final Optional<MemberEntity> original = memberRepository.findById(id);
        if (original.isPresent()) {
            return original.get();
        }
        return null;
    }


}
