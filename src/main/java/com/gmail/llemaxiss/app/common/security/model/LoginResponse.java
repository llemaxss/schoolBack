package com.gmail.llemaxiss.app.common.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "User authentication response")
public class LoginResponse implements Serializable {
  
  @Schema(
    description = "User id",
    example = "10000000-.."
  )
  private UUID id;
  
  @Schema(
    description = "User login",
    example = "admin"
  )
  private String username;
  
  @Schema(
    description = "JWT-token",
    example = "eyJhbGciOiJIUzUxMi.."
  )
  private String token;
  
  @Schema(
    description = "List of user roles",
    example = "[\"ROLE_ADMIN\"]"
  )
  private List<String> roles;
  
}
