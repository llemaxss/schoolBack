package com.gmail.llemaxiss.app.userContact.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gmail.llemaxiss.app._common.enums.common.CommonStringEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum ContactType implements CommonStringEnum {
  
  PHONE("PHONE"),
  EMAIL("EMAIL");
  
  @JsonValue
  private final String id;
  
}
