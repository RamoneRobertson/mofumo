package com.mofumo.api.services;

import com.mofumo.api.entities.User;
import com.mofumo.api.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class JwtService {
  private final UserRepository userRepository;
  @Value("${spring.jwt.secret}")
  private String secret;

  public String generateAccessToken(User user) {
    final long tokenExpiration = 300; // 5 minutes
    return generateToken(user, tokenExpiration);
  }

  public String generateRefreshToken(User user) {
    final long tokenExpiration = 604800; // 7 days
    return generateToken(user, tokenExpiration);
  }

  private String generateToken(User user, long tokenExpiration){
    return Jwts.builder()
            .subject(user.getId().toString())
            .claim("Email: ", user.getEmail())
            .claim("Last Name: ", user.getLastName())
            .claim("First Name: ", user.getFirstName())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .compact();
  }
  // the purpose of this method is to validate the generated Jwt tokens which gives access to protected endpoints
  public Boolean validateToken(String token) {
    // Here we try to parse the jwt and extract the payload
    try {
      var claims = getClaims(token);

      //If we have a valid token we need to make sure it is not expired
      return claims.getExpiration().after(new Date());
    }
    catch (JwtException ex) {
      // If we catch any exceptions it means the token is invalid and we return false
      return false;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  public Long getUserIdFromToken(String token) {
    return Long.valueOf(getClaims(token).getSubject());
  }
}
