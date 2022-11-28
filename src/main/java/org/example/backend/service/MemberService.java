package org.example.backend.service;

import org.example.backend.persistence.MemberRepository;
import org.example.backend.persistence.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    public void create(final MemberEntity entity) {
        MemberValidator.validate(entity);
        memberRepository.save(entity);
    }

    public static class MemberValidator {
        public static void validate(MemberEntity entity) {
            if (entity == null)
                throw new IllegalArgumentException("Entity can't be null");
        }
    }
}
