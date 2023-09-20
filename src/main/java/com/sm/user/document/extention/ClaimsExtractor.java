package com.sm.user.document.extention;


import io.jsonwebtoken.Claims;

public interface ClaimsExtractor {

    Claims extract(String token);

}
