package com.gmail.llemaxiss.app.role.enums;

import com.gmail.llemaxiss.app.common.enums.CommonStringEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum RoleType implements CommonStringEnum {

  ADMIN("ADMIN"),
  SYSTEM("SYSTEM"),
  DIRECTOR("DIRECTOR"),
  TEACHER("TEACHER"),
  STUDENT("STUDENT"),
  PARENT("PARENT"),
  USER("USER");

  private final String id;
}
