package com.gmail.llemaxiss.app.common.security.component.jwt;

import com.gmail.llemaxiss.app.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    if (
      path.startsWith(API_AUTH_URL_PART)
      || path.equals(ACTUATOR_HEALTH_URL_PART)
    ) {
      filterChain.doFilter(request, response);
      return;
    }

    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    String username = null;
    String jwt = null;

    if (
      authHeader != null
      && authHeader.startsWith(BEARER_)
    ) {
      jwt = authHeader.substring(BEARER_.length());
      username = jwtHelper.getUsernameFromJwtToken(jwt);
    }

    if (
      username != null
      && SecurityContextHolder.getContext()
        .getAuthentication() == null
    ) {
      UserDetails userDetails = userService.loadUserByUsername(username);

      if (jwtHelper.validateJwtToken(jwt)) {
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
      }
    }

    filterChain.doFilter(request, response);
  }
}
