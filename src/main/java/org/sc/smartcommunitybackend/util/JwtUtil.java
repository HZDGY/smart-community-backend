package org.sc.smartcommunitybackend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成和解析JWT token
 */
@Component
public class JwtUtil {

    /**
     * JWT密钥(从配置文件读取)
     */
    @Value("${jwt.secret:smart-community-secret-key-for-jwt-token-generation-must-be-at-least-256-bits}")
    private String secret;

    /**
     * JWT过期时间(毫秒) - 默认7天
     */
    @Value("${jwt.expiration:604800000}")
    private Long expiration;

    /**
     * 生成JWT Token
     *
     * @param userId 用户ID
     * @param phone 手机号
     * @return JWT token字符串
     */
    public String generateToken(Long userId, String phone) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("phone", phone);
        return createToken(claims, phone);
    }

    /**
     * 创建Token
     *
     * @param claims 载荷信息
     * @param subject 主题(一般是用户标识)
     * @return JWT token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    /**
     * 从Token中获取Claims
     *
     * @param token JWT token
     * @return Claims对象
     */
    public Claims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token JWT token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从Token中获取手机号
     *
     * @param token JWT token
     * @return 手机号
     */
    public String getPhoneFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("phone", String.class);
    }

    /**
     * 验证Token是否过期
     *
     * @param token JWT token
     * @return true-已过期, false-未过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 验证Token是否有效
     *
     * @param token JWT token
     * @param phone 手机号
     * @return true-有效, false-无效
     */
    public boolean validateToken(String token, String phone) {
        try {
            String tokenPhone = getPhoneFromToken(token);
            return tokenPhone.equals(phone) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}

