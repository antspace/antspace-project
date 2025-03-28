package com.ant.central.util;

import com.ant.central.config.JksProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Autowired
    private JksProperties jksProperties;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5分钟有效期
                .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                .compact();

        // 将令牌存储到 Redis
        redisTemplate.opsForValue().set(userDetails.getUsername(), token, 5, TimeUnit.MINUTES);
        return token;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenBlacklisted(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
        }
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 检查token是否即将过期(比如在5分钟内过期)
    public Boolean isTokenToExpire(String token, int minutes) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date(System.currentTimeMillis() + minutes * 60 * 1000));
    }

    // 检查令牌是否在黑名单中
    private Boolean isTokenBlacklisted(String token) {
        String username = extractUsername(token);
        String storedToken = redisTemplate.opsForValue().get(username);
        return storedToken == null || !storedToken.equals(token);
    }

    // 将令牌加入黑名单
    public void invalidateToken(String token) {
        String username = extractUsername(token);
        redisTemplate.delete(username);
    }

    private PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        try {
            InputStream is = JwtUtil.class.getResourceAsStream(jksProperties.getPath());
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(is, jksProperties.getKeyPassword().toCharArray());
            privateKey = (PrivateKey) keystore.getKey(jksProperties.getKeyAlias(), jksProperties.getKeyPassword().toCharArray());
        } catch (Exception e) {
        }
        return privateKey;
    }

    private PublicKey getPublicKey() {
        PublicKey publicKey = null;
        try {
            InputStream is = JwtUtil.class.getResourceAsStream(jksProperties.getPath());
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(is, jksProperties.getKeyPassword().toCharArray());
            publicKey = keystore.getCertificate(jksProperties.getKeyAlias()).getPublicKey();
        } catch (Exception e) {
        }
        return publicKey;
    }

}
