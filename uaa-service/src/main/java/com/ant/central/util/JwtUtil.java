package com.ant.central.util;

import com.ant.central.config.JksProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Autowired
    private JksProperties jksProperties;


    public String generateToken(UserDetails userDetails) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // 2 小时有效期
                .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
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

    private PrivateKey getPrivateKey() throws Exception {
        InputStream is = JwtUtil.class.getResourceAsStream(jksProperties.getPath());
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, jksProperties.getKeyPassword().toCharArray());
        return (PrivateKey) keystore.getKey(jksProperties.getKeyAlias(), jksProperties.getKeyPassword().toCharArray());
    }

    private PublicKey getPublicKey() throws Exception {
        InputStream is = JwtUtil.class.getResourceAsStream(jksProperties.getPath());
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, jksProperties.getKeyPassword().toCharArray());
        return keystore.getCertificate(jksProperties.getKeyAlias()).getPublicKey();
    }

}
