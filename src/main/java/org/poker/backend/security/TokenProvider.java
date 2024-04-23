package org.poker.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.poker.backend.persistence.entity.MemberEntity;
import org.poker.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenProvider {

    @Autowired
    private MemberService memberService;

    private static final String SECRET_KEY =
            "GolesadwweWWEQ2fasdDAasdsadaseqweasdaswqeFSDASFWqweSAqwasdasASDQWESDACXZCASDTEQRQWEASDZXCASDQWEASDZXCASWDEQWQWERQWDASDASDASD" +
                    "ASDASDASDASDASDASDASDASDASDASDASDASDASDAASXCXZTEWQTLKHJASIKDHAIUGVIUYASDGJKASDBNKJHQWRWQE";

    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    public String create(MemberEntity entity) {
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS));
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setSubject(entity.getId())
                .setIssuer("poker-play-worlds")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public MemberEntity validateAndGetMemberEntity(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return memberService.getById(claims.getSubject());
    }
}
