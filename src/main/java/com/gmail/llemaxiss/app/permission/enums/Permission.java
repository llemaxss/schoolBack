package com.gmail.llemaxiss.app.permission.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gmail.llemaxiss.app._common.enums.common.CommonStringEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum Permission implements CommonStringEnum {
  
  // ROLE //
  CREATE_ROLE("CREATE_ROLE"),
  READ_ROLE("READ_ROLE"),
  UPDATE_ROLE("UPDATE_ROLE"),
  DELETE_ROLE("DELETE_ROLE"),
  
  // USER //
  CREATE_USER("CREATE_USER"),
  READ_USER("READ_USER"),
  UPDATE_USER("UPDATE_USER"),
  DELETE_USER("DELETE_USER"),
  
  // USER_PROFILE //
  CREATE_USER_PROFILE("CREATE_USER_PROFILE"),
  READ_USER_PROFILE("READ_USER_PROFILE"),
  UPDATE_USER_PROFILE("UPDATE_USER_PROFILE"),
  DELETE_USER_PROFILE("DELETE_USER_PROFILE"),
  
  // USER_ROLE //
  CREATE_USER_ROLE("CREATE_USER_ROLE"),
  READ_USER_ROLE("READ_USER_ROLE"),
  DELETE_USER_ROLE("DELETE_USER_ROLE");
  
  @JsonValue
  private final String id;
  
}
