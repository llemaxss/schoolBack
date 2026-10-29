package com.gmail.llemaxiss.app.auth.service;

import com.gmail.llemaxiss.app._common.security.component.jwt.JwtHelper;
import com.gmail.llemaxiss.app._common.security.model.request.LoginRequest;
import com.gmail.llemaxiss.app._common.security.model.response.AppUserDetails;
import com.gmail.llemaxiss.app._common.security.model.response.LoginResponse;
import com.gmail.llemaxiss.app._common.service.CommonService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl extends CommonService implements AuthService {
  
  private final AuthenticationManager authenticationManager;
  
  private final JwtHelper jwtUtils;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public LoginResponse login(@NotNull LoginRequest loginRequest) {
    log.info("Login attempt for user: '{}'", loginRequest.getUsername());
    
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
      loginRequest.getUsername(),
      loginRequest.getPassword()
    );
    
    Authentication authentication = authenticationManager.authenticate(token);
    
    log.info("Authentication successful for user: '{}'", loginRequest.getUsername());
    
    SecurityContextHolder.getContext()
      .setAuthentication(authentication);
    
    AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
    
    String jwtToken = jwtUtils.generateJwtToken(userDetails.getUsername());
    
    log.info("JWT token generated for user: '{}'", loginRequest.getUsername());
    
    LoginResponse loginResponse = new LoginResponse();
    
    loginResponse.setId(userDetails.getId());
    loginResponse.setUsername(userDetails.getUsername());
    loginResponse.setToken(jwtToken);
    
    loginResponse.setRoles(
      userDetails.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .toList()
    );
    
    return loginResponse;
  }
}
