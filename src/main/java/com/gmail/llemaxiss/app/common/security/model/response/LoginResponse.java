package com.gmail.llemaxiss.app.common.security.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "User authentication response")
public class LoginResponse {
  
  @NotNull
  @Schema(
    description = "User id",
    example = "10000000-.."
  )
  private UUID id;
  
  @NotEmpty
  @Schema(
    description = "User login",
    example = "admin"
  )
  private String username;
  
  @NotEmpty
  @Schema(
    description = "JWT-token",
    example = "eyJhbGciOiJIUzUxMi.."
  )
  private String token;
  
  @NotNull
  @Schema(
    description = "List of user roles",
    example = "[\"ROLE_ADMIN\"]"
  )
  private List<String> roles;
  
}
