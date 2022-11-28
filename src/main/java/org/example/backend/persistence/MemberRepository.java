package org.example.backend.persistence;

import org.example.backend.persistence.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    MemberEntity findByEmail(String email);
    boolean existsByEmail(String email);
    MemberEntity findByEmailAndPassword(String email, String password);
}
