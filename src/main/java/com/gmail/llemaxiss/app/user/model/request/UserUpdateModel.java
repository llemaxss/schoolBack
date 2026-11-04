package com.gmail.llemaxiss.app.user.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Schema(description = "Request model for updating an existing user")
public class UserUpdateModel {
  
  @Schema(
    description = "Id of the user to update (usually passed in URL path)",
    example = "10000000-0000-0000-0000-000000000001",
    requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  private UUID id;
  
  @NotBlank
  @Schema(
    description = "User login",
    example = "admin",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String username;
  
}