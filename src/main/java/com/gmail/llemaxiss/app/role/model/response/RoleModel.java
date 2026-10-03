package com.gmail.llemaxiss.app.role.model.response;

import com.gmail.llemaxiss.app._common.model.response.CommonEntityModel;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Response model containing role information")
public class RoleModel extends CommonEntityModel {
  
  @Schema(description = "Name of the role", example = "Administrator")
  private String name;
  
  @Schema(description = "Type of the role", example = "ADMIN")
  private RoleType type;
  
}
