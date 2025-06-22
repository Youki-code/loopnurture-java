package org.springframework.samples.loopnurture.users.server.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * JWT工具类
 */
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtConfig jwtConfig;

    /**
     * 生成JWT token
     * @param user 用户信息
     * @param organizationId 当前选择的组织ID（可选）
     */
    public String generateToken(org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO user, String organizationId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("systemUserId", user.getSystemUserId());
        claims.put("userUniq", user.getUserUniq());
        claims.put("authType", user.getAuthType());
        if (StringUtils.hasText(organizationId)) {
            claims.put("organizationId", organizationId);
        }
        
        SignatureAlgorithm alg = SignatureAlgorithm.HS256;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(user.getSystemUserId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration() * 1000))
                .signWith(getSigningKey(), alg)
                .compact();
    }

    /**
     * 生成JWT token（不指定组织ID）
     */
    public String generateToken(org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO user) {
        return generateToken(user, null);
    }

    /**
     * 从token中获取用户ID
     */
    public Long getSystemUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 从token中获取组织ID
     */
    public String getOrganizationIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return (String) claims.get("organizationId");
    }

    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从token中获取Claims
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String getUserUniqFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userUniq", String.class);
    }
} 