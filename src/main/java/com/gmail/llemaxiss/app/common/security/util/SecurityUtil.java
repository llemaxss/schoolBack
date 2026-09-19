package com.gmail.llemaxiss.app.common.security.util;

import com.gmail.llemaxiss.app.common.enums.ErrorCode;
import com.gmail.llemaxiss.app.common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app.common.property.component.AppProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtil {

  /**
   * Returns the username of the currently authenticated user
   */
  @NotEmpty
  public static String getCurrentUsername() {
    Authentication authentication = getAuthentication();

    return authentication.getName();
  }

  /**
   * Checks if the current user has the specified role
   */
  public static boolean hasRole(@NotNull String role) {
    Authentication authentication = getAuthentication();

    String rolePrefix = AppProperty.SPRING_ROLE_PREFIX;

    String targetRole = role.startsWith(rolePrefix)
      ? role
      : rolePrefix + role;

    return authentication.getAuthorities()
      .stream()
      .map(GrantedAuthority::getAuthority)
      .anyMatch(authority ->
        authority.equals(targetRole)
      );
  }

  @NotNull
  private static Authentication getAuthentication() {
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
