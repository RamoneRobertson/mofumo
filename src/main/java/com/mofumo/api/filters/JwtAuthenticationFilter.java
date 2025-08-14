package com.mofumo.api.filters;

import com.mofumo.api.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
      filterChain.doFilter(request, response);
      return;
    }

    // Extract the JWT token (remove "Bearer " prefix)
    var token = authHeader.replace("Bearer ", "");

    // Validate the token using your JwtService
    if(!jwtService.validateToken(token)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      filterChain.doFilter(request, response);
      return;
    }

    // We need to create an authentication object
    var authentication = new UsernamePasswordAuthenticationToken(
            // First we need to get the subject
            jwtService.getUserIdFromToken(token),
            // Now we need credentials (null for now)\
            null,
            null
    );

    authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
    );

    // SecurityContextHolder store info about the current authenticated user
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Now we pass the request to the next filter in the chaink
    filterChain.doFilter(request, response);
  }
}
