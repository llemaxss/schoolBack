package com.gmail.llemaxiss.app.userProfile.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gmail.llemaxiss.app.common.enums.common.CommonStringEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum Gender implements CommonStringEnum {
  MALE("MALE"),
  FEMALE("FEMALE");
  
  @JsonValue
  private final String id;
  
}
