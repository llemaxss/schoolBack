package com.gmail.llemaxiss.app._common.security.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "User authentication request")
public class LoginRequest {
  
  @NotBlank
  @Schema(
    description = "User login",
    example = "admin",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String username;

  @NotBlank
  @Schema(
    description = "User password",
    example = "admin123",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String password;
  
}
