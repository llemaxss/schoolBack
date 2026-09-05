package com.gmail.llemaxiss.app.role.enums;

import com.gmail.llemaxiss.app.common.enums.CommonStringEnum;
import com.gmail.llemaxiss.app.permission.enums.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.collections4.SetUtils;

import java.util.Set;

@AllArgsConstructor
@ToString
@Getter
public enum RoleType implements CommonStringEnum {

  ADMIN("ADMIN", Set.of(Permission.values())),
  SYSTEM("SYSTEM", Set.of(Permission.values())),
  DIRECTOR("DIRECTOR", SetUtils.emptySet()),
  TEACHER("TEACHER", SetUtils.emptySet()),
  STUDENT("STUDENT", SetUtils.emptySet()),
  PARENT("PARENT", SetUtils.emptySet()),
  USER("USER", SetUtils.emptySet());

  private final String id;
  
  private final Set<Permission> permissions;
  
}
