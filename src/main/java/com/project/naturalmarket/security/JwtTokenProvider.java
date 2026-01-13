package com.project.naturalmarket.security;


import com.project.naturalmarket.exception.MarketAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;


    public String generateToken(Authentication authentication){

        String username= authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority).toList();

        Date currentDate=new Date();

        Date expireDate=new Date(currentDate.getTime()+jwtExpirationDate);

        String token= Jwts.builder().claim("roles", roles).setSubject(username).setIssuedAt(new Date())
                .setExpiration(expireDate).signWith(key()).compact();

        return token;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsername(String token){

        Claims claims=Jwts.parserBuilder()
                      .setSigningKey(key())
                      .build().parseClaimsJws(token)
                      .getBody();

        String username= claims.getSubject();

        return username;
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new MarketAPIException(HttpStatus.BAD_REQUEST,"Expired JWT token");
        } catch (MalformedJwtException e) {
            throw new MarketAPIException(HttpStatus.BAD_REQUEST,"Invalid JWT token");
        } catch (UnsupportedJwtException e) {
            throw new MarketAPIException(HttpStatus.BAD_REQUEST,"Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new MarketAPIException(HttpStatus.BAD_REQUEST,"JWT claims string is empty");
        }
    }

}
