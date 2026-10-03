package com.gmail.llemaxiss.app.user.util;

import com.gmail.llemaxiss.app._common.component.AppProperty;
import com.gmail.llemaxiss.app._common.security.model.response.AppUserDetails;
import com.gmail.llemaxiss.app._common.security.util.SecurityUtil;
import com.gmail.llemaxiss.app.user.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserUtil {
  
  /**
   * Returns the username of the currently authenticated user
   */
  @NotEmpty
  public static String getUsername() {
    Authentication authentication = SecurityUtil.getAuthentication();
    
    return authentication.getName();
  }
  
  /**
   * Returns the {@link AppUserDetails} of the currently authenticated user
   */
  @NotNull
  public static AppUserDetails getAppUserDetails() {
    Authentication authentication = SecurityUtil.getAuthentication();
    
    return (AppUserDetails) authentication.getPrincipal();
  }
  
  /**
   * Checks if the current user has the specified role
   */
  public static boolean hasRole(@NotNull String role) {
    String rolePrefix = AppProperty.SPRING_ROLE_PREFIX;
    
    String targetRole = role.startsWith(rolePrefix)
      ? role
      : rolePrefix + role;
    
    return hasAuthority(targetRole);
  }
  
  /**
   * Checks if the current user has the specified authority
   */
  public static boolean hasAuthority(@NotNull String authority) {
    Authentication authentication = SecurityUtil.getAuthentication();
    
    return authentication.getAuthorities()
      .stream()
      .map(GrantedAuthority::getAuthority)
      .anyMatch(userAuthority ->
        userAuthority.equals(authority)
      );
  }
  
  /**
   * Returns authorities of the currently authenticated user
   */
  @NotNull
  public static Collection<? extends GrantedAuthority> getAuthorities() {
    Authentication authentication = SecurityUtil.getAuthentication();
    
    return authentication.getAuthorities();
  }
  
  /**
   * Returns authorities of {@link User}
   */
  @NotNull
  public static List<? extends GrantedAuthority> getAuthorities(@NotNull User user) {
    return user.getUserRoles()
      .stream()
      .map(userRole ->
        userRole.getRole()
          .getType()
      )
      .flatMap(role -> {
        Stream<GrantedAuthority> roleAuth = Stream.of(
          new SimpleGrantedAuthority(AppProperty.SPRING_ROLE_PREFIX + role.getId())
        );
        
        Stream<GrantedAuthority> permAuth = role.getPermissions()
          .stream()
          .map(perm ->
            new SimpleGrantedAuthority(perm.getId())
          );
        
        return Stream.concat(roleAuth, permAuth);
      })
      .collect(Collectors.toList());
  }
  
}
