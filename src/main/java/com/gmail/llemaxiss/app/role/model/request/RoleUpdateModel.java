package com.gmail.llemaxiss.app.role.model.request;

import com.gmail.llemaxiss.app.role.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Schema(description = "Request model for updating an existing role")
public class RoleUpdateModel {
  
  @Schema(
    description = "Id of the role to update (usually passed in URL path)",
    example = "10000000-0000-0000-0000-000000000001",
    requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  private UUID id;
  
  @NotEmpty
  @Schema(
    description = "New name of the role",
    example = "Super Administrator",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String name;
  
  @NotNull
  @Schema(
    description = "New type of the role",
    example = "ADMIN",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private RoleType type;
  
}
