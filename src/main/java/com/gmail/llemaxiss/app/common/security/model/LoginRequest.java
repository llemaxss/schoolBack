package com.gmail.llemaxiss.app.common.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Schema(description = "User authentication request")
public class LoginRequest implements Serializable {
  
  @NotEmpty
  @Schema(
    description = "User login",
    example = "admin",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String username;

  @NotEmpty
  @Schema(
    description = "User password",
    example = "admin123",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String password;
  
}
