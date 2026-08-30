package com.gmail.llemaxiss.app.common.security.component.jwt;

import com.gmail.llemaxiss.app.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.gmail.llemaxiss.app.common.property.component.AppProperty.API_URL_PART;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  
  private static final String API_AUTH_URL_PART = API_URL_PART + "/auth";
  private static final String ACTUATOR_HEALTH_URL_PART = "/actuator/health";

  private static final String BEARER_ = "Bearer ";

  @Autowired
  private JwtHelper jwtHelper;

  @Autowired
  private UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    String path = request.getRequestURI();
    
    LOGGER.debug("Processing request for path: '{}'", path);

    if (
      path.startsWith(API_AUTH_URL_PART)
      || path.equals(ACTUATOR_HEALTH_URL_PART)
    ) {
      LOGGER.debug("Skipping auth for path: '{}'", path);
      filterChain.doFilter(request, response);
      return;
    }

    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    LOGGER.debug("Authorization header: '{}'", authHeader);
    
    String username = null;
    String jwt = null;

    if (
      authHeader != null
      && authHeader.startsWith(BEARER_)
    ) {
      jwt = authHeader.substring(BEARER_.length());
      username = jwtHelper.getUsernameFromJwtToken(jwt);
      
      LOGGER.debug("Extracted username from token: '{}'", username);
    }

    if (
      username != null
      && SecurityContextHolder.getContext()
        .getAuthentication() == null
    ) {
      LOGGER.info("Authenticating user: '{}'", username);
      
      UserDetails userDetails = userService.loadUserByUsername(username);

      if (jwtHelper.validateJwtToken(jwt)) {
        LOGGER.info("JWT token is valid for user: '{}'", username);
        
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );

        authToken.setDetails(
          new WebAuthenticationDetailsSource()
            .buildDetails(request)
        );

        SecurityContextHolder.getContext()
          .setAuthentication(authToken);
        
        LOGGER.info("User '{}' authenticated and set in SecurityContext", username);
      } else {
        LOGGER.info("JWT token is not valid for user: '{}'", username);
      }
    }

    filterChain.doFilter(request, response);
  }
}
