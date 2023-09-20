//package com.sm.user.document.extention;
//
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@Data
//public class JwtSettings {
//
//    public final static String CLAIM_ROLES = "roles";
//    public final static String CLAIM_SCOPES = "scopes";
//    public final static String CLAIM_USERID = "userid";
//    public final static String CLAIM_CLIENTID = "clientid";
//    public final static String CLAIM_DOMAIN = "domain";
//    public final static String CLAIM_PATIENTID = "patientId";
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private Long tokenExpiration;
//    @Value("${link.expiration}")
//    private Long linkExpireration;
//
//    @Value("${jwt.header:Authorization}")
//    private String tokenHeader;
//    @Value("${jwt.query.param:token}")
//    private String tokenQueryParam;
//
//    @Value("${jwt.prefix}")
//    private String tokenPrefix;
//
//    @Value("${jwt.issuer}")
//    private String tokenIssuer;
//
//}
