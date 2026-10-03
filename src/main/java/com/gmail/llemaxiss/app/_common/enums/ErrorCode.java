package com.gmail.llemaxiss.app._common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gmail.llemaxiss.app._common.enums.common.CommonStringEnum;
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
  INTERNAL_ERROR("INTERNAL_ERROR"),
  VALIDATION_FAILED("VALIDATION_FAILED"),
  AUTHENTICATED_USER_NOT_FOUND("AUTHENTICATED_USER_NOT_FOUND"),
  
  // ROLE //
  ROLE_NOT_FOUND("ROLE_NOT_FOUND"),
  ROLE_NAME_ALREADY_EXISTS("ROLE_NAME_ALREADY_EXISTS"),
  
  // USER //
  USER_NOT_FOUND("USER_NOT_FOUND"),
  
  // USER_PROFILE //
  USER_PROFILE_NOT_FOUND("USER_PROFILE_NOT_FOUND"),
  USER_PROFILE_ALREADY_EXISTS("USER_PROFILE_ALREADY_EXISTS"),
  ;
  
  @JsonValue
  private final String id;
  
}
