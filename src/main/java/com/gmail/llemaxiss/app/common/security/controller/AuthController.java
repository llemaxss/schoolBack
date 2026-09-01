package com.gmail.llemaxiss.app.common.security.controller;


import com.gmail.llemaxiss.app.common.security.component.jwt.JwtHelper;
import com.gmail.llemaxiss.app.common.security.model.AppUserDetails;
import com.gmail.llemaxiss.app.common.security.model.LoginRequest;
import com.gmail.llemaxiss.app.common.security.model.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
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

import static com.gmail.llemaxiss.app.common.property.component.AppProperty.API_URL_PART;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_URL_PART + "/auth")
@Tag(
  name = "Auth",
  description = "Authentication and user session management"
)
public class AuthController {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
  
  private final AuthenticationManager authenticationManager;

  private final JwtHelper jwtUtils;

  @PostMapping("/login")
  @Operation(
    summary = "Log in",
    description = "Accepts login and password, returns JWT token and user information"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Successful login",
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = LoginResponse.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Incorrect login or password",
      content = @Content()
    )
  })
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    LOGGER.info("Login attempt for user: '{}'", loginRequest.getUsername());
    
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
      loginRequest.getUsername(),
      loginRequest.getPassword()
    );

    Authentication authentication = authenticationManager.authenticate(token);
    
    LOGGER.info("Authentication successful for user: '{}'", loginRequest.getUsername());
    
    SecurityContextHolder.getContext()
      .setAuthentication(authentication);

    AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

    String jwtToken = jwtUtils.generateJwtToken(userDetails.getUsername());
    
    LOGGER.info("JWT token generated for user: '{}'", loginRequest.getUsername());
    
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

    return ResponseEntity.ok(loginResponse);
  }
  
}
