package com.gmail.llemaxiss.app._common.security.component;

import com.gmail.llemaxiss.app.permission.enums.Permission;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import com.gmail.llemaxiss.app.user.util.UserUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AccessChecker {
  
  /**
   * Checks current user has the {@link Permission}
   */
  public boolean hasPermission(@NotNull Permission permission) {
    return UserUtil.hasAuthority(permission.getId());
  }
  
  /**
   * Checks current user has at least one of the {@link Permission}
   */
  public boolean hasAnyPermission(@NotNull Permission... permissions) {
    return Arrays.stream(permissions)
      .anyMatch(this::hasPermission);
  }
  
  /**
   * Checks current user has the {@link RoleType}
   */
  public boolean hasRoleType(@NotNull RoleType roleType) {
    return UserUtil.hasRole(roleType.getId());
  }
  
  /**
   * Checks current user has at least one of the {@link RoleType}
   */
  public boolean hasAnyRoleType(@NotNull RoleType... roleTypes) {
    return Arrays.stream(roleTypes)
      .anyMatch(this::hasRoleType);
  }
  
}
