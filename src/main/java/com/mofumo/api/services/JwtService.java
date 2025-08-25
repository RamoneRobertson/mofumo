package com.mofumo.api.services;

import com.mofumo.api.config.JwtConfig;
import com.mofumo.api.entities.User;
import com.mofumo.api.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
  private final UserRepository userRepository;
  private final JwtConfig jwtConfig;

  public String generateAccessToken(User user) {
    return generateToken(user, jwtConfig.getAccessTokenExpiration());
  }

  public String generateRefreshToken(User user) {
    return generateToken(user, jwtConfig.getRefreshTokenExpiration());
  }

  private String generateToken(User user, long tokenExpiration){
    return Jwts.builder()
            .subject(user.getId().toString())
            .claim("Email: ", user.getEmail())
            .claim("Last Name: ", user.getLastName())
            .claim("First Name: ", user.getFirstName())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
            .signWith(jwtConfig.getSecretKey())
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
            .verifyWith(jwtConfig.getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  public Long getUserIdFromToken(String token) {
    return Long.valueOf(getClaims(token).getSubject());
  }
}
