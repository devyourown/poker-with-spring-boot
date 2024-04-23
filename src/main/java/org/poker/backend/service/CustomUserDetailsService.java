package org.poker.backend.service;

import lombok.RequiredArgsConstructor;
import org.poker.backend.persistence.MemberRepository;
import org.poker.backend.persistence.entity.MemberEntity;
import org.poker.backend.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity member =  memberRepository.findByNickname(username);
        if (member == null)
            throw new UsernameNotFoundException("User is not existed.");
        return new CustomUserDetails(member);
    }
}
