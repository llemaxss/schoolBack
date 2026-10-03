package com.gmail.llemaxiss.app._common.security.util;

import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.exception.model.response.CommonException;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtil {
  
  @NotNull
  public static Authentication getAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext()
      .getAuthentication();
    
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CommonException(
        ErrorCode.AUTHENTICATED_USER_NOT_FOUND,
        "No authenticated user found in security context"
      );
    }
    
    return authentication;
  }
  
}
