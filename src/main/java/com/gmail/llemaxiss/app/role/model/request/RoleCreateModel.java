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

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Schema(description = "Request model for creating a new role")
public class RoleCreateModel {
  
  @NotEmpty
  @Schema(
    description = "Unique name of the role",
    example = "Administrator",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String name;
  
  @NotNull
  @Schema(
    description = "Type of the role",
    example = "ADMIN",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  private RoleType type;
  
}
