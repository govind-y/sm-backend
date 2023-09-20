package com.sm.user.document.extention;

import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.List;

public interface TokenParser {

    List<? extends GrantedAuthority> getAuthorities(String token);

    String getSubject(String token);

    Instant getExpiration(String token);

    String getAudience(String token);

    String getIssuer(String token);

    Instant getIssuedAt(String token);

    String getDomain(String token);
    Long getUserId(String token);
    Long getClientId(String token);
    Long getPatientId(String token);

    //Set<Context> getContexts(String token);

}
