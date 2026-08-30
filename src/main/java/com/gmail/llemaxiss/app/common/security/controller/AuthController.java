package com.gmail.llemaxiss.app.common.security.controller;


import com.gmail.llemaxiss.app.common.security.component.jwt.JwtHelper;
import com.gmail.llemaxiss.app.common.security.model.AppUserDetails;
import com.gmail.llemaxiss.app.common.security.model.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.gmail.llemaxiss.app.common.property.component.AppProperty.API_URL_PART;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_URL_PART + "/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;

  private final JwtHelper jwtUtils;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
      loginRequest.getUsername(),
      loginRequest.getPassword()
    );

    Authentication authentication = authenticationManager.authenticate(token);

    SecurityContextHolder.getContext()
      .setAuthentication(authentication);

    AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

    String jwtToken = jwtUtils.generateJwtToken(userDetails.getUsername());

    Map<String, Object> response = new HashMap<>();

    response.put("token", jwtToken);
    response.put("id", userDetails.getId());
    response.put("username", userDetails.getUsername());
    response.put(
      "roles",
      userDetails.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .toList()
    );

    return ResponseEntity.ok(response);
  }
}
