package com.gmail.llemaxiss.app.unit._common;

import com.gmail.llemaxiss.app._common.component.AppProperty;
import com.gmail.llemaxiss.app._common.security.component.AccessChecker;
import com.gmail.llemaxiss.app.permission.enums.Permission;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AccessCheckerTest {
  
  private final AccessChecker accessChecker = new AccessChecker();
  
  @AfterEach
  public void tearDown() {
    SecurityContextHolder.clearContext();
  }
  
  @Test
  public void hasPermissionWhenAuthorityPresent() {
    Permission authority = Permission.CREATE_ROLE;
    
    authenticate(authority.getId());
    
    assertThat(
      accessChecker.hasPermission(authority)
    )
      .isTrue();
  }
  
  @Test
  public void hasNoPermissionWhenAuthorityAbsent() {
    authenticate(Permission.READ_USER_ROLE.getId());
    
    assertThat(
      accessChecker.hasPermission(Permission.CREATE_ROLE)
    )
      .isFalse();
  }
  
  @Test
  public void hasAnyPermissionWhenOneOfAuthoritiesPresent() {
    Permission authority = Permission.READ_ROLE;
    
    authenticate(authority.getId());
    
    assertThat(
      accessChecker.hasAnyPermission(
        Permission.CREATE_ROLE, authority
      )
    ).isTrue();
  }
  
  @Test
  public void hasRoleTypeByRolePrefix() {
    RoleType roleType = RoleType.ADMIN;
    
    authenticate(AppProperty.SPRING_ROLE_PREFIX + roleType.getId());
    
    assertThat(
      accessChecker.hasRoleType(roleType)
    )
      .isTrue();
    
    assertThat(
      accessChecker.hasRoleType(RoleType.TEACHER)
    )
      .isFalse();
  }
  
  private void authenticate(String... authorities) {
    SecurityContextHolder.getContext()
      .setAuthentication(
        new UsernamePasswordAuthenticationToken(
          "user",
          null,
          Arrays.stream(authorities)
            .map(SimpleGrantedAuthority::new)
            .toList()
        )
      );
  }
}
