package com.gmail.llemaxiss.app.role.mapper;

import com.gmail.llemaxiss.app.common.mapper.CommonMapper;
import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.model.response.RoleModel;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoleMapper {

  @NotNull
  public static RoleModel toModelFromEntity(@NotNull Role role) {
    RoleModel roleModel = new RoleModel();
    
    CommonMapper.mapEntityFields(roleModel, role);
    
    roleModel.setName(role.getName());
    roleModel.setType(role.getType());
    
    return roleModel;
  }

}
