package com.gmail.llemaxiss.app.common.security.util;

import com.gmail.llemaxiss.app.role.service.RoleService;
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
  public static String getCurrentUsername() throws IllegalStateException {
    Authentication authentication = getAuthentication();

    return authentication.getName();
  }

  /**
   * Checks if the current user has the specified role
   */
  public static boolean hasRole(@NotNull String role) {
    Authentication authentication = SecurityContextHolder.getContext()
      .getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return false;
    }

    String rolePrefix = RoleService.ROLE_PREFIX;

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

  private static Authentication getAuthentication() throws IllegalStateException {
    Authentication authentication = SecurityContextHolder.getContext()
      .getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException("No authenticated user found in security context");
    }

    return authentication;
  }
  
}
