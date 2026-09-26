package com.gmail.llemaxiss.app.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gmail.llemaxiss.app.common.enums.common.CommonStringEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Enumeration of business error codes used across the application
 *
 * <p>
 * Each code has a unique string identifier that can be used by the frontend
 * to display localized error messages.
 * </p>
 */
@AllArgsConstructor
@ToString
@Getter
public enum ErrorCode implements CommonStringEnum {
  
  // COMMON //
  VALIDATION_FAILED("VALIDATION_FAILED"),
  INTERNAL_ERROR("INTERNAL_ERROR"),
  AUTHENTICATED_USER_NOT_FOUND("AUTHENTICATED_USER_NOT_FOUND"),
  
  // ROLE //
  ROLE_NOT_FOUND("ROLE_NOT_FOUND"),
  ROLE_NAME_ALREADY_EXISTS("ROLE_NAME_ALREADY_EXISTS"),
  
  // USER //
  USER_NOT_FOUND("USER_NOT_FOUND"),
  ;
  
  @JsonValue
  private final String id;
  
}
