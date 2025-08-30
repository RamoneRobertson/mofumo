package com.mofumo.api.filters;

import com.mofumo.api.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // First we need to extract the authorization header from the request
    var authHeader = request.getHeader("Authorization");

    // If there is no header or it doesn't start with Bearer, skip
    if(authHeader == null || !authHeader.startsWith("Bearer ")) {
      // Send to the next filter in the chain
      filterChain.doFilter(request, response);
      return;
    }

    // Extract the JWT token (remove "Bearer " prefix)
    var token = authHeader.replace("Bearer ", "");
    var jwt = jwtService.parseToken(token);

    // Validate the token, check if the token is null or expired
    if(jwt == null || jwt.isExpired()) {
      // Send the request to the next chain in the filter
      filterChain.doFilter(request, response);
      return;
    }

    // We need to create an authentication object
    var authentication = new UsernamePasswordAuthenticationToken(
            // subject (username or id, typically)
            jwt.getUserId(),
            // Now we need credentials (null for now)
            null,
            // Set authorities to the role of the user in this format ROLE_<roleName>
            List.of(new SimpleGrantedAuthority("ROLE_" + jwt.getRole()))
    );

    authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
    );

    // SecurityContextHolder store info about the current authenticated user
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Now we pass the request to the next filter in the chain
    filterChain.doFilter(request, response);
  }
}
