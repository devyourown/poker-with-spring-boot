package org.poker.backend.persistence;

import org.poker.backend.persistence.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    MemberEntity findByEmail(String email);
    boolean existsByEmail(String email);
    MemberEntity findByEmailAndPassword(String email, String password);

    MemberEntity findByNickname(String nickname);
}
