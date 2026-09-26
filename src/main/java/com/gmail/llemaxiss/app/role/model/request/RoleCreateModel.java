package com.gmail.llemaxiss.app.role.model.request;

import com.gmail.llemaxiss.app.role.enums.RoleType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoleCreateModel {
  
  @NotEmpty
  private String name;
  
  @NotNull
  private RoleType type;
  
}
