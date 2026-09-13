package com.gmail.llemaxiss.app.permission.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gmail.llemaxiss.app.common.enums.CommonStringEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum Permission implements CommonStringEnum {
  
  ;
  
  @JsonValue
  private final String id;
  
}
