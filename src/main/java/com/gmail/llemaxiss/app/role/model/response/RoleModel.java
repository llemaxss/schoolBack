package com.gmail.llemaxiss.app.role.model.response;

import com.gmail.llemaxiss.app.common.model.response.CommonEntityModel;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleModel extends CommonEntityModel {
  
  private String name;
  
  private RoleType type;
  
}
