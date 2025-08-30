package com.mofumo.api.config;

import com.mofumo.api.enums.Role;
import com.mofumo.api.filters.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  // Required for AuthenticationManager to forward auth requests
  @Bean
  public AuthenticationProvider authenticationProvider() {
    // First create a provider
    var provider = new DaoAuthenticationProvider();

    // The DaoProvider requires a passwordEncoder and userDetailsService, so set them before returning
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(userDetailsService);
    return provider;
  }

  // Entry point for authentication
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // Use stateless session (token based auth)
    http.sessionManagement(c ->
                c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Disable CSRF
        .csrf(AbstractHttpConfigurer::disable)
        // Authorize (Define which endpoints are public or private)
        .authorizeHttpRequests(c -> c
                // General endpoints
                .requestMatchers("/").permitAll()
                .requestMatchers(HttpMethod.GET, "/index.html").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()

                // Role specific endpoints
                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name()) // only admins can access /admin
                .requestMatchers("/provider/**").hasRole(Role.PROVIDER.name()) // only providers can access provider

                .anyRequest().authenticated()
        )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c -> {
                    c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    c.accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                    });
                });

    return http.build();
  }
}
