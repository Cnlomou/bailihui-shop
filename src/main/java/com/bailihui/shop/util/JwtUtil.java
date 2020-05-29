package com.bailihui.shop.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author Cnlomou
 * @create 2020/5/26 20:32
 */
public class JwtUtil {
    //过期时间
    private static final Long EXP_TIME = 60 * 60 * 1000L;
    //颁发者
    private static final String ISSUER = "Linmo";
    //签名方式
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    //盐
    private static final String KEY = "Linmo";

    public static String buildJwt(String subject, Object object) {
        return buildJwt(subject, object, (String) null);
    }

    public static String buildJwt(String subject, Object object, String key) {
        return buildJwt(subject, object, null, key);
    }

    public static String buildJwt(String subject, Object object, Long expiration) {
        return buildJwt(subject, object, expiration, null);
    }

    public static String buildJwt(String subject, Object object, Long expiration, String key) {
        long expTime = 0L;
        //取得过期时间
        if (expiration != null)
            expTime = expiration;
        else
            expTime = EXP_TIME;

        //取得放入的对象
        Map claims = null;
        if (object instanceof Map)
            claims = (Map) object;
        else
            claims = Collections.singletonMap(object.getClass().getSimpleName(), object);

        //构建jwt令牌
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .setId(UUID.randomUUID().toString())
                .setSubject(subject)
                .setIssuer(ISSUER)
                .addClaims(claims)
                .signWith(SIGNATURE_ALGORITHM, key == null ? KEY : key)
                .compact();
    }

    public static Jws<Claims> parser(String jwt) throws Exception {
        return parser(jwt, null);
    }

    public static Jws<Claims> parser(String jwt, String key) throws Exception {
        return Jwts.parser().setSigningKey(key == null ? KEY : key).parseClaimsJws(jwt);
    }

    public static String getSubject(String jwt) throws Exception {
        return getSubject(parser(jwt));
    }

    public static String getSubject(Jws<Claims> jws) {
        return jws.getBody().getSubject();
    }
}
