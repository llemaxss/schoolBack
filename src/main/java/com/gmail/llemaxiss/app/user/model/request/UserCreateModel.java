package com.gmail.llemaxiss.app.user.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Schema(description = "Request model for creating a new user")
public class UserCreateModel {
  
  @NotBlank
  @Schema(
    description = "User login",
    example = "admin",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String username;
  
  @ToString.Exclude
  @NotBlank
  @Schema(
    description = "Temporary password",
    example = "abcDEF123!",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String password;
  
}