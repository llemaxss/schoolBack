package com.gmail.llemaxiss.app.role.model.request;

import com.gmail.llemaxiss.app.role.enums.RoleType;
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
@Getter
@Setter
@ToString
public class RoleUpdateModel {
  
  private UUID id;
  
  @NotEmpty
  private String name;
  
  @NotNull
  private RoleType type;
  
}
