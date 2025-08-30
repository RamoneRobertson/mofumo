package com.mofumo.api.services;

import com.mofumo.api.config.JwtConfig;
import com.mofumo.api.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
  private final JwtConfig jwtConfig;

  public Jwt generateAccessToken(User user) {
    return generateToken(user, jwtConfig.getAccessTokenExpiration());
  }
  public Jwt generateRefreshToken(User user) {
    return generateToken(user, jwtConfig.getRefreshTokenExpiration());
  }

  private Jwt generateToken(User user, long tokenExpiration){
    var claims = Jwts.claims().subject(user.getId().toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 *tokenExpiration))
            .add("email", user.getEmail())
            .add("firstName", user.getFirstName())
            .add("lastName", user.getLastName())
            .add("role", user.getRole())
            .build();

    return new Jwt(claims, jwtConfig.getSecretKey());
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
            .verifyWith(jwtConfig.getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  public Jwt parseToken(String token){
    try {
      var claims = getClaims(token);
      return new Jwt(claims, jwtConfig.getSecretKey());
    }
    catch(JwtException e){
      return null;
    }
  }
}
